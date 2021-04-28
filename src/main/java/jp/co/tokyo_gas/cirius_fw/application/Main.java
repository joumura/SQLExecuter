package jp.co.tokyo_gas.cirius_fw.application;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * 入力された引数や出力様式などを制御する.
 */
public class Main {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Main main = new Main();
		if (0 == args.length) {
			main.usage();
		}
		for (String arg : args) {
			if (arg.startsWith("pass:")) {
				System.out.println("Your crypted password is " + CryptUtil.encrypt(arg.substring(5)));
			} else if (arg.equals("in:")) {
				main.execute(main.getSQLfromStdin());
			} else {
				main.executeSQLFile(arg);
			}
		}
	}

	Properties props;

	Main() throws IOException {
		props = new Properties();
		props.load(new FileReader("SQLExecuter.propertites"));
	}

	String getSQLfromStdin() throws ClassNotFoundException, IOException {
		StringBuilder sqlBuild = new StringBuilder();
		try (Scanner scanner = new Scanner(System.in)) {
			String line = "";
			do {
				line = scanner.nextLine();
				sqlBuild.append(line).append(" ").append(System.lineSeparator());
			} while (!line.contains(";"));
		}
		return sqlBuild.toString();
	}

	void executeSQLFile(String sqlFileName) throws ClassNotFoundException, IOException {
		List<String> lines = Files.readAllLines(Paths.get(sqlFileName));
		String sql = String.join(" " + System.lineSeparator(), lines);
		execute(sql);
	}

	void execute(String sql) throws ClassNotFoundException, IOException {
		try (DBAccess dba = new DBAccess(props.getProperty("driver"), props.getProperty("url"),
			props.getProperty("user"), CryptUtil.decrypt(props.getProperty("password")))) {
			dba.outputResult(sql);
		}
	}

	void usage() {
		System.err.println("usage:");
		System.err.println("   java -jar SQLExecuter-x.x.x.jar select.sql update.sql ...");
		System.err.println();
		System.err.println(" As preparation... crypt your DB password !!!");
		System.err.println("   java -jar SQLExecuter-x.x.x.jar pass:<your password>");
		System.exit(1);
	}

}
