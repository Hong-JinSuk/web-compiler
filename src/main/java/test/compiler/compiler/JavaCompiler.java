package test.compiler.compiler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;

@Service
@Slf4j
public class JavaCompiler {
    public String runCode(String code, String input) {
        StringBuilder output = new StringBuilder();
        File tempDir = null;
        try {
            // 임시 디렉토리 생성
            tempDir = Files.createTempDirectory("java_temp").toFile();

            // 임시 파일에 Java 코드 작성
            File tempFile = new File(tempDir, "Main.java");
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(code);
            }

            // Java 파일 컴파일
            ProcessBuilder compilePb = new ProcessBuilder("javac", tempFile.getAbsolutePath());
            Process compileProcess = compilePb.start();
            compileProcess.waitFor();

            // 컴파일 에러 읽기
            BufferedReader compileErrorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            String line;
            while ((line = compileErrorReader.readLine()) != null) {
                output.append("COMPILATION ERROR: ").append(line).append("\n");
            }

            if (compileProcess.exitValue() != 0) {
                return output.toString();
            }

            // Java 파일 실행
            ProcessBuilder runPb = new ProcessBuilder("java", "-cp", tempDir.getAbsolutePath(), "Main");
            Process runProcess = runPb.start();

            // Java 실행 입력 제공
            try (BufferedWriter runWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
                String[] inputs = input.split("\\s+");
                for (String inputLine : inputs) {
                    runWriter.write(inputLine);
                    runWriter.newLine();
                }
                runWriter.flush();
                runWriter.close(); // 입력 스트림을 닫음
            }

            // Java 실행 출력 읽기
            BufferedReader runReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            while ((line = runReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Java 실행 에러 읽기
            BufferedReader runErrorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
            while ((line = runErrorReader.readLine()) != null) {
                output.append("RUNTIME ERROR: ").append(line).append("\n");
            }

            // 프로세스 종료 상태 확인
            int exitCode = runProcess.waitFor();
            output.append("Exited with code: ").append(exitCode);

        } catch (Exception e) {
            output.append("Exception: ").append(e.getMessage());
        } finally {
            // 임시 디렉토리 삭제
            if (tempDir != null && tempDir.exists()) {
                for (File file : tempDir.listFiles()) {
                    file.delete();
                }
                tempDir.delete();
            }
        }
        return output.toString();
    }
}