/* groovylint-disable UnnecessaryGetter */
package web_server.services

import web_server.domain.LogLine

import grails.gorm.transactions.TransactionService

import groovy.transform.CompileStatic
import javax.inject.Singleton
import javax.inject.Inject
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

import java.util.regex.*

@Singleton
@CompileStatic
class ShellAccessService {

    @Inject TransactionService transactionService;

    Process process = null
    // SERVER OUTPUT
    BufferedReader br

    //SERVER INPUT
    StringBuffer postedCommands = new StringBuffer();
    BufferedWriter bw

    // Threads
    Thread processReadingThread
    Thread processWritingThread
    File serverDir = new File('d:\\Files\\Minecraft\\1.16.2 Server')

    void startShell() {
        ProcessBuilder pb = new ProcessBuilder(
            "java",
            "-Xmx3072M",
            "-Xms1024M",
            "-jar",
            "server.jar",
            "nogui"
        )
        pb.directory(serverDir)
        pb.redirectErrorStream(true)
        process = pb.start()
         // Set up the reader/writer
        br = new BufferedReader(new InputStreamReader(process.getInputStream()))
        bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))
    }

    void addToPostedCommands(String text) {
        postedCommands.append(text)
    }

    void processReader() {
        processReadingThread = Thread.start {
            String line = null
            while ((line = br.readLine()) != null) {
                try {
                    transactionService.withTransaction {
                        // Save log line
                        Pattern p = Pattern.compile('(\\[(.*?)\\]) (\\[(.*?)\\]): (.*)')
                        Matcher m = p.matcher(line)
                        m.matches()
                        String timestamp = m.group(2)
                        String context = m.group(4)
                        String rest = m.group(5)

                        LogLine ll = new LogLine(
                            serverTimeStamp: timestamp,
                            context: context,
                            output: rest
                        )
                        ll.save()
                    }
                } catch (Exception e) {
                    println("Whoops saving log line: ${e.message}")
                }
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

    boolean isAlive() {
        if (process != null) {
            return process.isAlive()
        }
        return false
    }
}
