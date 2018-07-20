/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arquivo;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;

/**
 *
 * @author izaquiellopesdebessas
 */
public class Arquivo {

    /**
     * Cria um novo arquivo no local indicado, com o conteúdo, nome e extensão passados por parâmetro
     * 
     * @param url caminho onde deverá criar o arquivo
     * @param nome nome do arquivo a ser criado
     * @param conteudo conteúdo do arquivo
     * @param ext extensão do arquivo
     */
    public void criar(String url, String nome, String conteudo, String ext) {
        FileWriter fw = null;
        try {
            File file = new File(url + nome + ext);
            file.createNewFile();

            fw = new FileWriter(file);
            fw.write(conteudo);
        } catch (IOException ex) {
            Logger.getLogger(Arquivo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Arquivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Recebe-se um arquivo (tipo file) e retorna-se uma string com o tipo do arquivo passado por parâmetro
     * 
     * @param file File - arquivo que terá seu tipo validado
     * @return String - tipo do arquivo
     */
    public String MimeTypeFile(File file) {
        return new MimetypesFileTypeMap().getContentType(file);
    }

    /**
     * Ler o arquivo especificado no caminho indicado pelo parâmetro de entrada
     * 
     * @param arq indica onde está o arquivo que deverá ser lido
     * @return retorna o conteúdo do arquivo
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String ler(String arq) throws FileNotFoundException, IOException {
        String file = "";
        FileReader fr = new FileReader(arq);
        BufferedReader br = new BufferedReader(fr);
        while (br.ready()) {
            file += br.readLine() + '\n';
        }
        return file;
    }

    /**
     * Cria um objeto de propriedades as quais podem ser adicionadas num arquivo
     * 
     * @param args propriedades do arquivo
     * @return Properties - retorna-se um objeto de propriedades
     */
    public Properties createPropertyFile(String[] args) {
        Properties prop = new Properties();

        for (int i = 0; i < args.length; i++) {
            prop.setProperty(args[i], args[i++]);
        }

        return prop;
    }

    /**
     * Salva as propriedades em um determinado arquivo
     * 
     * @param p Propriedades que serão adicionadas ao arquivo
     * @param f Arquivo que receberá as propriedades
     * @param description Descrição a ser adicionada no arquivo
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void savePropertyFile(Properties p, File f, String description) throws FileNotFoundException, IOException {
        try (OutputStream propFile = new FileOutputStream(f)) {
            p.store(propFile, description);
        }
    }

    /**
     * Retorna-se as propriedades do arquivo passado por parâmetro
     * 
     * @param f Arquivo que terá as propriedades lidas
     * @return Properties - retorna-se as propriedades do arquivo
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public Properties loadPropertyFile(File f) throws FileNotFoundException, IOException {
        Properties prop;
        try (InputStream propFile = new FileInputStream(f)) {
            prop = new Properties();
            prop.load(propFile);
        }

        return prop;
    }

    /**
     * Passa-se uma propriedade por parâmetro, a qual terá seus argumentos editados
     * 
     * @param p Propriedades que serão editadas
     * @return Propriedades resultantes da edição
     */
    public Properties alterPropertyFile(Properties p) {
        Properties newP = new Properties();
        Enumeration enP = p.propertyNames();
        String key;

        while (enP.hasMoreElements()) {
            key = (String) enP.nextElement();

            if (!key.equals("fake_entry")) {
                if (key.equals("log_level")) {
                    newP.setProperty(key, "3");
                } else {
                    newP.setProperty(key, p.getProperty(key));
                }
            }
        }

        return newP;
    }

    /**
     * Bloqueia um dado arquivo
     * 
     * @param file Arquivo
     * @param permissoes Permissões do arquivo
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void bloquear(File file, String permissoes) throws FileNotFoundException, IOException {
        //recupera o canal do arquivo
        FileChannel channel = new RandomAccessFile(file, permissoes).getChannel();

        //usa o canal do arquivo para tentar criar uma trava para este, caso ainda este não possua nenhuma
        FileLock lock = channel.lock(0, Long.MAX_VALUE, true);
        //tenta adiquirir o bloqueio do arquivo caso este já esteja bloqueado
        lock = channel.tryLock(0, Long.MAX_VALUE, true);
    }

    /**
     * Desbloqueia um arquivo, deve-se passar a trava e o canal
     * 
     * @param lock bloqueio do arquivo
     * @param channel canal do arquivo
     * @throws IOException 
     */
    public void desbloquear(FileLock lock, FileChannel channel) throws IOException {
        //libera a tranca do arquivo
        lock.release();
        //fecha o canal do arquivo
        channel.close();
    }

    /**
     * Verifica se um determinado arquivo está aberto
     * 
     * @param file Arquivo a ser verificado
     * @return retorna-se true caso o arquivo esteja aberto, e false, caso contrário
     * @throws FileNotFoundException 
     */
    public boolean isOpenFile(File file) throws FileNotFoundException {
        FileChannel channel = new RandomAccessFile(file, null).getChannel();
        return channel.isOpen();
    }

    /**
     * Verifica se o arquivo bloqueado está compartilhado, ou se é de uso exclusivo
     * 
     * @param file Arquivo
     * @return retorna-se true caso esteja compartilhado e false, caso contrário
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public boolean isSharedFile(File file) throws FileNotFoundException, IOException {
        FileChannel channel = new RandomAccessFile(file, null).getChannel();
        FileLock lock = channel.lock();
        return lock.isShared();
    }
}
