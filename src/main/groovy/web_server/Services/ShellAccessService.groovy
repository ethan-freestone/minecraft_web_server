/* groovylint-disable UnnecessaryGetter */
package web_server.services

import groovy.transform.CompileStatic
import javax.inject.Singleton
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

@Singleton
@CompileStatic
class ShellAccessService {

    Process process = null
    // SERVER OUTPUT
    BufferedReader br
    File consoleFile

    //SERVER INPUT
    StringBuffer postedCommands = new StringBuffer();
    BufferedWriter bw

    //FILE READER
    BufferedReader fbr

    // Threads
    Thread processReadingThread
    Thread processWritingThread

    void startShell() {
        ProcessBuilder pb = new ProcessBuilder(
            "java",
            "-Xmx3072M",
            "-Xms1024M",
            "-jar",
            "server.jar",
            "nogui"
        )
        pb.directory(new File('d:\\Files\\Minecraft\\1.16.2 Server'))
        pb.redirectErrorStream(true)
        process = pb.start()
         // Set up the reader/writer
        br = new BufferedReader(new InputStreamReader(process.getInputStream()))
        bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))
    }

    void setupConsoleFile() {
        consoleFile = new File('console.txt')
            if (consoleFile.createNewFile()) {
                println("File created: " + consoleFile.getName() + consoleFile.getPath())
            } else {
                println("File already exists.")
            }
        fbr = new BufferedReader(new FileReader(consoleFile))
    }

    List<String> readConsoleFile() {
        return Files.readAllLines(Paths.get(consoleFile.getPath()))
    }

    void addToPostedCommands(String text) {
        postedCommands.append(text)
    }

    void processReader() {
        processReadingThread = Thread.start {
            FileWriter consoleWriter = new FileWriter(consoleFile)
            String line = null
            while ((line = br.readLine()) != null) {
                BufferedWriter consoleFileBW = new BufferedWriter(consoleWriter)
                consoleFileBW.write("$line \n")
                consoleFileBW.flush()
            }
        }
    }

    void processWriter() {
        processWritingThread = Thread.start {
            while (process != null){
                while (postedCommands.length() != 0) {
                    bw.write(postedCommands.substring(0))
                    bw.newLine()
                    postedCommands.delete(0, postedCommands.length())
                    bw.flush()
                }
            }
        }
    }
}
