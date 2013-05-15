/*
 **
 **  Mar. 24, 2009
 **
 **  The author disclaims copyright to this source code.
 **  In place of a legal notice, here is a blessing:
 **
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 **
 **                                         Stolen from SQLite :-)
 **  Any feedback is welcome.
 **  Kohei TAKETA <k-tak@void.in>
 **
 */
package net.moraleboost.tinysegmenter;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.io.*;

public class ModelExporter
{
    // java ModelExporter tiny_segmenter_source.js exporter.js out.java
    public static void main(String[] args)
    throws Exception
    {
        try {
            Context ctx = ContextFactory.getGlobal().enterContext();
            Scriptable scope = ctx.initStandardObjects();

            evaluateSource(ctx, scope, args[0]);
            evaluateSource(ctx, scope, args[1]);
            
            emit(ctx, scope, args[2]);
        } finally {
            Context.exit();
        }
    }
    
    private static void evaluateSource(Context ctx, Scriptable scope, String filename)
    throws Exception
    {
        FileInputStream fis = null;
        InputStreamReader isr = null;

        try {
            File f = new File(filename);
            fis = new FileInputStream(f);
            isr = new InputStreamReader(fis, "utf-8");
            ctx.evaluateReader(scope, isr, f.getName(), 1, null);
        } finally {
            if (isr != null) {
                try { isr.close(); } catch (Exception ignored) {}
            }
            if (fis != null) {
                try { fis.close(); } catch (Exception ignored) {}
            }
        }
    }
    
    private static void emit(Context ctx, Scriptable scope, String filename)
    throws Exception
    {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            File f = new File(filename);
            fos = new FileOutputStream(f);
            osw = new OutputStreamWriter(fos, "utf-8");
            emitToWriter(ctx, scope, osw);
        } finally {
            if (osw != null) {
                try { osw.close(); } catch (Exception ignored) {}
            }
            if (fos != null) {
                try { fos.close(); } catch (Exception ignored) {}
            }
        }
    }
    
    private static void emitToWriter(Context ctx, Scriptable scope, Writer w)
    throws Exception
    {
        emitPrologue(w);
        
        String[] names = {
                "BC1", "BC2", "BC3",
                "BP1", "BP2",
                "BQ1", "BQ2", "BQ3", "BQ4",
                "BW1", "BW2", "BW3",
                "TC1", "TC2", "TC3", "TC4",
                "TQ1", "TQ2", "TQ3", "TQ4",
                "TW1", "TW2", "TW3", "TW4",
                "UC1", "UC2", "UC3", "UC4", "UC5", "UC6",
                "UP1", "UP2", "UP3",
                "UQ1", "UQ2", "UQ3",
                "UW1", "UW2", "UW3", "UW4", "UW5", "UW6"
        };
        
        Function getKeys = (Function)scope.get("getKeys", scope);
        Function getValues = (Function)scope.get("getValues", scope);
        Object bias = scope.get("BIAS", scope);
        
        w.write("    public static final int BIAS = " + Context.toString(bias) + ";\r\n");
        for (String name: names) {
            Object keys = Context.toString(getKeys.call(ctx, scope, scope, new String[] {name}));
            Object vals = Context.toString(getValues.call(ctx, scope, scope, new String[] {name}));
            w.write("    public static final String[] " + name + "_KEYS" + " = " + keys + ";\r\n");
            w.write("    public static final Integer[] " + name + "_VALS" + " = " + vals + ";\r\n");
        }
        
        for (String name: names) {
            w.write("    public static final Map<String, Integer> " + name + ";\r\n");
        }
        
        emitStaticBlock(w, names);

        emitEpilogue(w);
    }
    
    private static void emitPrologue(Writer w)
    throws Exception
    {
        w.write("// Automatically generated. Do not edit.\r\n");
        w.write("package net.moraleboost.tinysegmenter;\r\n\r\n");
        w.write("import java.util.Collections;\r\n");
        w.write("import java.util.Map;\r\n");
        w.write("import java.util.HashMap;\r\n\r\n");
        w.write("public class TinySegmenterConstants\r\n");
        w.write("{\r\n");
    }
    
    private static void emitEpilogue(Writer w)
    throws Exception
    {
        w.write("}\r\n");
    }
    
    private static void emitStaticBlock(Writer w, String[] names)
    throws Exception
    {
        w.write("    static {\r\n");
        w.write("        int i;\r\n");
        w.write("        Map<String, Integer> m;\r\n");
        
        for (String name: names) {
            w.write("        ");
            w.write("m = new HashMap<String, Integer>();\r\n");
            w.write("        ");
            w.write("for (i=0; i<" + name + "_KEYS.length; ++i) {\r\n");
            w.write("        ");
            w.write("    m.put(" + name + "_KEYS[i], " + name + "_VALS[i]);\r\n");
            w.write("        ");
            w.write("}\r\n");
            w.write("        ");
            w.write(name + " = Collections.unmodifiableMap(m);\r\n");
        }
        
        w.write("    }\r\n");
    }
}
