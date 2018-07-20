/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arquivo;

import java.io.File;

/**
 *
 * @author izaquiel
 */
public class ExtensaoArquivo extends javax.swing.filechooser.FileFilter {

    String[] extensions;
    String description;

    /**
     * Controle de extensão de arquivos
     * 
     * @param descr descrição do arquivo
     * @param exts extensão do arquivo
     */
    public ExtensaoArquivo(String descr, String exts) {
        extensions = new String[exts.length()];
        for (int i = exts.length() - 1; i >= 0; i--) {
            Character x;
            x = exts.charAt(i);
            extensions[i] = x.toString();
        }

        if (descr == null) {
            Character x = exts.charAt(0);
            description = x.toString() + "files";
        } else {
            description = descr;
        }
    }

    /**
     * sobreescrita do método accept do JFileChooser do Java
     * 
     * @param f File f, arquivo que deve ser passado para validação
     * @return return true se for validado como arquivo ou diretório correto, senão, retorna false
     */
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        for (String extension : extensions) {
            if (f.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }

        return false;
    }

    /**
     * sobreescrita do método getDescription();
     * 
     * @return String return description;
     */
    @Override
    public String getDescription() {
        return description;
    }
}
