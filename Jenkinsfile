 node {
   try{
//      stage 'clone project'
//      git branch: 'PRODV02',credentialsId: '51319b71-5bbc-46d3-a55a-1ca6ef05d362',url: 'https://gitlab.com/CamelSoft/thejobbackend.git'

     stage 'checkout project'
     checkout scm

     stage 'check env'
     sh "mvn -v"
     sh "java -version"

     stage 'clean package'
     sh "mvn clean package"

     stage 'reload daemon'
     sh "echo 'manM3423M' | sudo -S systemctl daemon-reload"

     stage 'start daemon'
     sh "echo 'manM3423M' | sudo -S systemctl start rayaserver"

     stage 'enable daemon'
     sh "echo 'manM3423M' | sudo -S systemctl enable rayaserver"

     stage 'restart daemon'
     sh "echo 'manM3423M' | sudo -S systemctl restart rayaserver"
   }catch(e){
     throw e;
   }
 }

