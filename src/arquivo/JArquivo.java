/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arquivo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;

/**
 *
 * @author izaquiellopesdebessas
 */
public class JArquivo {

    /**
     * Cria um novo arquivo vazio, é usado o JFileChooser para selecionar um diretório e criar o arquivo desejado
     * 
     * @param ext extensão do arquivo
     * @return retorna true caso crie o arquivo com sucesso, e false caso contrário
     * @throws IOException
     */
    public boolean novo(String[] ext) throws IOException {
        JFileChooser escolhe = new JFileChooser();

        for (String ext_item : ext) {
            escolhe.setFileFilter(new ExtensaoArquivo(ext_item.toLowerCase(), ext_item.toUpperCase()));
        }

        escolhe.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (escolhe.showSaveDialog(escolhe) == JFileChooser.APPROVE_OPTION) {
            File arq = escolhe.getSelectedFile();
            try (FileWriter fw = new FileWriter(arq)) {
                fw.write("");
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Abre o JFileChooser para que o usuário selecione qual arquivo deseja abrir, editar ou visualizar.
     * 
     * @param ext lista de extensões aceitas
     * @return retorna o caminho/url do arquivo escolhido, para que o sistema possa fazer a leitura
     * @throws IOException
     */
    public String abrir(String[] ext) throws IOException {
        String arquivo = "";
        JFileChooser escolhe = new JFileChooser();

        for (String ext_item : ext) {
            escolhe.setFileFilter(new ExtensaoArquivo(ext_item.toLowerCase(), ext_item.toUpperCase()));
        }

        escolhe.showOpenDialog(escolhe);

        if (escolhe.isAcceptAllFileFilterUsed()) {
            File arq = escolhe.getSelectedFile();
            if (arq != null) {
                arquivo = arq.getAbsolutePath();
            }
            return arquivo;
        } else {
            return arquivo;
        }
    }

    /**
     * Deve-se passar o conteúdo e selecionar o caminho e/ou arquivo o qual irá ser salvo, ou sobreescrito no
     * JFileChooser do Java
     * 
     * @param arquivo conteúdo do arquivo a ser salvo
     * @param ext extensão do arquivo a ser salvo
     * @return retorna o conteúdo salvo
     * @throws IOException
     */
    public String salvar(String arquivo, String[] ext) throws IOException {
        JFileChooser escolhe = new JFileChooser();

        for (String ext_item : ext) {
            escolhe.setFileFilter(new ExtensaoArquivo(ext_item.toLowerCase(), ext_item.toUpperCase()));
        }

        if (escolhe.showSaveDialog(escolhe) == JFileChooser.APPROVE_OPTION) {
            File arq = escolhe.getSelectedFile();
            try (FileWriter fw = new FileWriter(arq)) {
                fw.write(arquivo);
            }
            return arquivo;
        } else {
            return arquivo;
        }
    }
}
