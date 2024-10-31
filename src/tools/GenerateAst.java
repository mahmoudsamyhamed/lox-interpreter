package tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expr right"
        ));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);

        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        defineVisitor(writer, baseName, types);

        writer.println();
        writer.println("  abstract <R> R accept(Visitor<R> visitor);");

        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        writer.println("}");
        writer.close();
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fields) throws IOException {
        writer.println("  static class " + className + " extends " + baseName + " {");
        writer.println("    " + className + "(" + fields + ")" + "{");

        String[] fieldsList = fields.split(", ");

        for(String field : fieldsList) {
            String name = field.split(" ")[1];
            writer.println("      this." + name + " = " + name + ";");
        }
        writer.println("    }");

        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("      return visitor.visit" +
                className + baseName + "(this);");
        writer.println("    }");

        writer.println();
        for (String field : fieldsList) {
            writer.println("    final " + field + ";");
        }
        writer.println("  }");
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) throws IOException {
        writer.println("  interface Visitor<R> {");
        for (String type : types) {
            String typename = type.split(":")[0].trim();
            writer.println("    R visit" + typename + baseName + "(" + typename + " " + baseName.toLowerCase()  + ");");
        }
        writer.println("  }");
    }
}
