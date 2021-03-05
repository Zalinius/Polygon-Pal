
void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: env.GIT_URL],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}


pipeline {
	agent any
	stages {
		// Note that the agent automatically checks out the source code from Github	

    	stage('Compile') { 
            steps {
            	sh 'mvn --batch-mode compile'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn --batch-mode test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Package Jar') {
            steps {
                sh 'mvn --batch-mode -DskipTests clean package'
            }
        }
        stage('Deploy') {
        	when {
 				branch 'main'
	       	}
			environment {
				GAME_VERSION = sh script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true 
				PROJECT_NAME = sh script: 'mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout', returnStdout: true 
				
				JAVA_8_HOME = '/usr/lib/jvm/java-8-openjdk-amd64/bin'
				LAUNCH4J_HOME = '/usr/local/bin/launch4j'
				JRE_WIN = '/usr/local/bin/OpenJDK11U-jre_x64_windows_hotspot_11.0.10_9.zip'

				PACKR_HOME = '/usr/local/bin/packr'
				JRE_LINUX = '/usr/local/bin/jdk-11.0.10+9-jre-linux/' //pre unpacked
			}
			steps {
				//Make EXE
				sh 'mkdir target/windows'
				sh '${JAVA_8_HOME}/java -jar ${LAUNCH4J_HOME}/launch4j.jar windows_exe_config.xml'
				
				//Make Linux program
				sh 'mkdir target/linux'
				sh 'java -jar ${PACKR_HOME}/packr-all-3.0.3.jar --classpath target/${PROJECT_NAME}-${GAME_VERSION}.jar  --platform linux64	 --executable "Polygon Pal"  --output target/linux  --jdk ${JRE_LINUX}  --mainclass com.zalinius.polygonpal.PolygonPalGame'
				
				//Get JRE
				unzip zipFile: '/usr/local/bin/OpenJDK11U-jre_x64_windows_hotspot_11.0.10_9.zip', dir: 'target/windows/jre/'
				
				sh 'sudo butler push target/windows/ 							zalinius/polygonpal:windows 	  -i /home/zalinius/.config/itch/butler_creds --userversion $GAME_VERSION --fix-permissions --if-changed'				
				sh 'sudo butler push target/linux/  							zalinius/polygonpal:linux   	  -i /home/zalinius/.config/itch/butler_creds --userversion $GAME_VERSION --fix-permissions --if-changed'				
				sh 'sudo butler push target/${PROJECT_NAME}-${GAME_VERSION}.jar zalinius/polygonpal:win-linux-mac -i /home/zalinius/.config/itch/butler_creds --userversion $GAME_VERSION --fix-permissions --if-changed'
	       	}
	    }
	}
	post {
    	success {
       		setBuildStatus('Build succeeded', 'SUCCESS');
    	}
    	failure {
       		setBuildStatus('Build failed', 'FAILURE');
    	}		
	}
}