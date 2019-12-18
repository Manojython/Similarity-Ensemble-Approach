### in R #####

install.packages("pgirmess")
install.packages("vioplot")

library(pgirmess)
library(vioplot)

curDataDir<-"/Users/qiong/chunting/projects/kinaseAnalysis/codes/4PDSP/4R"

curDataDir="C:\\Users\\qxc30\\Downloads\\4PDSP\\4Comparison"
#"C:\\Users\\qxc30\\4PDSP\\testing"

groupMapF = file.path(curDataDir, "all.group2gid.tab.txt")
groupMapM = as.matrix(read.table(groupMapF,header=TRUE,sep="\t", as.is=TRUE))

cliqueV = as.numeric( unique(groupMapM[,6]) )
for ( clique in cliqueV ){
    groupIDV = as.numeric( groupMapM[as.numeric( groupMapM[,6] ) == clique, 1 ] )
    classesM = as.matrix( groupMapM[as.numeric( groupMapM[,6] ) == clique, 4:5 ] )
    classesGroupM = cbind(groupIDV, classesM)

    classe1Name = groupMapM[as.numeric( groupMapM[,6] ) == clique, 2 ] 
    classe2Name = groupMapM[as.numeric( groupMapM[,6] ) == clique, 3 ] 

    classesGroupNameM = cbind(classesGroupM, classe1Name, classe2Name)

    # find unique classes
    classesV = as.numeric( unique (classesM [as.numeric( classesM[,1]) ==  as.numeric( classesM[, 2] ), 1 ] ))
    classesNamesV = unique( classesGroupNameM[ as.numeric( classesGroupNameM[, 2]) == classesV, 4 ] )
    classesMUniq = cbind(classesV, classesNamesV)

     # within groups
     withinMatrix = matrix( 1, length(classesV), length(classesV) )
     for ( classID1 in as.numeric( classesMUniq[,1 ] ) ){
          group1ID = as.numeric( classesGroupNameM[ as.numeric( classesGroupNameM[, 2] ) == classID1 & as.numeric( classesGroupNameM[, 3] ) == classID1 , 1] )
          #group1F =  file.path(curDataDir, paste( clique, "=",group1ID, ".all.group.txt", sep="") )
          #group1M = as.matrix(read.table(group1F, header=FALSE, sep=" ", as.is=TRUE))
          score1F =  file.path(curDataDir, paste( clique, "-", group1ID, ".all.score.txt", sep="" ) )
          score1M = as.matrix(read.table(score1F, header=FALSE, sep=" ", as.is=TRUE))
          #scoreGroup1 = cbind( score1M[, 1], group1M[, 1] )

          for ( classID2 in as.numeric( classesMUniq[,1 ] ) ){
               group2ID = as.numeric( classesGroupNameM[ as.numeric( classesGroupNameM[, 2]) == classID2 & as.numeric( classesGroupNameM[, 3] ) == classID2 , 1] )
               #group2F =  file.path(curDataDir, paste( clique, "-", group2ID, ".all.group.txt", sep="") )
               #group2M = as.matrix(read.table(group2F,header=FALSE,sep=" ", as.is=TRUE))
               score2F =  file.path(curDataDir, paste( clique, "-", group2ID, ".all.score.txt", sep="" ) )
               score2M = as.matrix(read.table(score2F,header=FALSE,sep=" ", as.is=TRUE))
               #scoreGroup2 = cbind( score2M[, 1], group2M[, 1] )

               if ( group1ID != group2ID ){
                    withinMatrix[ group1ID, group2ID ] = t.test( score1M[, 1], score2M[, 1] )$p.value
               }
          }
     }
     # write to the file
     write.table( withinMatrix, file=paste(curDataDir, "\results\", clique, "_within_groups.txt", sep=""), eol="\r\n", row.names = classesMUniq[, 2] , col.names = classesMUniq[, 2] , sep="\t")

     # betweenness 

}


allgroupF =  file.path(curDataDir, "all.group.txt")
allscoreF =  file.path(curDataDir, "all.score.txt")

allscoreM = as.matrix(read.table(allscoreF,header=FALSE,sep=" ", as.is=TRUE))
allgroupM = as.matrix(read.table(allgroupF,header=FALSE,sep=" ", as.is=TRUE))

allscoreV <-as.numeric(allscoreM[,1])
allgroupV <-as.numeric(allgroupM[,1])
allScoreGroup = cbind(allscoreV, allgroupV)
groupIDs = as.numeric( groupMapM[,1] )

meanM=matrix(0, 4,4)
pvalueM=matrix(0, 4,3)

G11 = allScoreGroup[allScoreGroup[,2] == 1, 1] #G-G
G12 = allScoreGroup[allScoreGroup[,2] == 2, 1] #G-I I-G
G13 = allScoreGroup[allScoreGroup[,2] == 3, 1] #G-K  K-G
G14 = allScoreGroup[allScoreGroup[,2] == 4, 1] #G-N   N-G

pdf(paste(curDataDir, "\\GPCR_distribution.pdf", sep=""), bg="white", width=7,height=7)

plot(1,1, xlim = c(0,5), ylim = range(c(G11,G12,G13,G14)), type = 'n', xlab='Pairwise IDG families', ylab='Tc scores of pairwise ligands active for IDG families', xaxt = 'n')

vioplot(G11, G12, G13,G14, add=TRUE, col=c("gray"))
axis(side=1, at=1:4, labels= c("GPCR-GPCR", "GPCR-IC", "GPCR-K", "GPCR-NR"))

dev.off()


meanM[1,1] = mean(G11)
meanM[1,2] = mean(G12)
meanM[1,3] = mean(G13)
meanM[1,4] = mean(G14)

pvalueM[1,1] = t.test(G11,G12)$p.value
pvalueM[1,2] = t.test(G11,G13)$p.value
pvalueM[1,3] = t.test(G11,G14)$p.value

I21  = allScoreGroup[allScoreGroup[,2] == 5, 1]  #I-I
I22 = allScoreGroup[allScoreGroup[,2] == 2, 1]    #I-G
I23 = allScoreGroup[allScoreGroup[,2] == 7, 1]    #I-K
I24  = allScoreGroup[allScoreGroup[,2] == 6, 1]  #I-N   N-I

pdf(paste(curDataDir, "\\IC_distribution.pdf", sep=""), bg="white", width=7,height=7)

plot(1,1, xlim = c(0,5), ylim = range(c(I21, I22, I23, I24)), type = 'n', xlab='Pairwise IDG families', ylab='Tc scores of pairwise ligands active for IDG families', xaxt = 'n')

vioplot(I21 , I22, I23,I24, add=TRUE, col="gold")
axis(side=1, at=1:4, labels=c("IC-IC", "IC-GPCR", "IC-K", "IC-NR"))

dev.off()


meanM[2,1] = mean(I21)
meanM[2,2] = mean(I22)
meanM[2,3] = mean(I23)
meanM[2,4] = mean(I24)

pvalueM[2,1] = t.test(I21,I22)$p.value
pvalueM[2,2] = t.test(I21,I23)$p.value
pvalueM[2,3] = t.test(I21,I24)$p.value

k31 = allScoreGroup[allScoreGroup[,2] == 8, 1]   #K-K
k32 = allScoreGroup[allScoreGroup[,2] == 3, 1]
k33 = allScoreGroup[allScoreGroup[,2] == 7, 1]   #K-I
k34 = allScoreGroup[allScoreGroup[,2] == 9, 1]   #K-N

pdf(paste(curDataDir, "\\kinase_distribution.pdf", sep=""), bg="white", width=7,height=7)

plot(1,1, xlim = c(0,5), ylim = range(c(k31, k32, k33, k34)), type = 'n', xlab='Pairwise IDG families', ylab='Tc scores of pairwise ligands active for IDG families', xaxt = 'n')

vioplot(k31, k32, k33, k34, add=TRUE, col=c("blue"))
axis(side=1, at=1:4, labels= c("K-K", "K-GPCR", "K-IC","K–NR"))

dev.off()


meanM[3,1] = mean(k31)
meanM[3,2] = mean(k32)
meanM[3,3] = mean(k33)
meanM[3,4] = mean(k34)

pvalueM[3,1] = t.test(k31,k32)$p.value
pvalueM[3,2] = t.test(k31,k33)$p.value
pvalueM[3,3] = t.test(k31,k34)$p.value

n41 = allScoreGroup[allScoreGroup[,2] == 10, 1]   #N-N
n42 = allScoreGroup[allScoreGroup[,2] == 4, 1]    #N-G
n43 = allScoreGroup[allScoreGroup[,2] == 6, 1]    #N-I
n44 = allScoreGroup[allScoreGroup[,2] == 9, 1]    #N-K

selfMeanM = matrix(0, 4, 4)
selfMeanM[1,1] = t.test(G11,G11)$p.value
selfMeanM[1,2] = t.test(G11,I22)$p.value
selfMeanM[1,3] = t.test(G11,k33)$p.value
selfMeanM[1,4] = t.test(G11,n44)$p.value
selfMeanM[2,1] = t.test(I22,G11)$p.value
selfMeanM[2,2] = t.test(I22,I22)$p.value
selfMeanM[2,3] = t.test(I22,k33)$p.value
selfMeanM[2,4] = t.test(I22,n44)$p.value
selfMeanM[3,1] = t.test(k33,G11)$p.value
selfMeanM[3,2] = t.test(k33,I22)$p.value
selfMeanM[3,3] = t.test(k33,k33)$p.value
selfMeanM[3,4] = t.test(k33,n44)$p.value
selfMeanM[4,1] = t.test(n44,G11)$p.value
selfMeanM[4,2] = t.test(n44,I22)$p.value
selfMeanM[4,3] = t.test(n44,k33)$p.value
selfMeanM[4,4] = t.test(n44,n44)$p.value


pdf(paste(curDataDir, "\\NR_distribution.pdf", sep=""), bg="white", width=7,height=7)

plot(1,1, xlim = c(0,5), ylim = range(c(n41, n42, n43, n44)), type = 'n', xlab='Pairwise IDG families', ylab='Tc scores of pairwise ligands active for IDG families', xaxt = 'n')

vioplot(n41, n42, n43, n44, add=TRUE, col= "green")
axis(side=1, at=1:4, labels= c("NR-NR", " NR-GPCR ", "NR- IC", "NR-K"))

dev.off()


meanM[4,1] = mean(k31)
meanM[4,2] = mean(k32)
meanM[4,3] = mean(k33)
meanM[4,4] = mean(k34)

pvalueM[4,1] = t.test(n41,n42)$p.value
pvalueM[4,2] = t.test(n41,n43)$p.value
pvalueM[4,3] = t.test(n41,n44)$p.value


pdf(paste(curDataDir, "/all_distribution.pdf", sep=""), bg="white", width=7,height=7)
par(mar=c(5,5,4,2)+0.1,font.lab=1,lwd=2,bty="n")



test1 = kruskal.test(allscoreV ~ allgroupV)
test1A = kruskalmc(allscoreV ~ allgroupV)

test2 = aov(allscoreV ~ allgroupV)

hist(allscoreV,xlab="Compound-to-compound structural similarity",100, ylab="Frequency",col="lightblue",main="", cex.axis=1.7,cex.lab=1.8)

for (id in groupIDs){
     hist(allscoreV,xlab=paste(groupMapM[as.numeric(groupMapM[,1])==id,2], "-to-", groupMapM[as.numeric(groupMapM[,1])==id,3], " structural similarity"),100, ylab="Frequency",col="lightblue",main="", cex.axis=1.7,cex.lab=1.8)
     print(id)
}

dev.off()


