package Utils;

import android.util.Log;

import java.util.Vector;

/**
 * Created by TI on 23/03/2016.
 */
public class CriaTabela {

    private int versaoBanco;
    private Vector<String> vCriaTabelas;

    // Construtor da Classe
    public CriaTabela(int versaoDB) {

        Log.d("LoadSystem - Tabelas", "Sql para Criação das Tabelas");

        vCriaTabelas = new Vector<String>();

        // Chama os metodos q contem as SQL
        createTable();
        this.versaoBanco = versaoDB;

    }

    public Vector<String> pegaTabelas() {

        Log.d("LoadSystem - Tabels", "Pegando as tabelas");
        return this.vCriaTabelas;
    }

    // Cria as tabelas e coloca no vertor de tabelas
    private void createTable() {

        String tbProgramacao = "CREATE TABLE PROGRAMACAO (ID_PROGRAMACAO INTEGER NOT NULL PRIMARY KEY,"
                + "HORARIOS_PROG VARCHAR (50),"
                + "DESCRICAO_PROG VARCHAR (255),"
                + "DIASEMANA_PROG VARCHAR (50))";

        vCriaTabelas.add(tbProgramacao);


        String insereProgramacao1 = "INSERT INTO PROGRAMACAO (HORARIOS_PROG, DESCRICAO_PROG, DIASEMANA_PROG) VALUES "
                + "('06:00 `as 9:20', 'De segunda a sexta', 'Sertanejo')";

        vCriaTabelas.add(insereProgramacao1);

        String insereProgramacao2 = "INSERT INTO PROGRAMACAO (HORARIOS_PROG, DESCRICAO_PROG, DIASEMANA_PROG) VALUES "
                + "('09:20 `as 10:00', 'De segunda a sexta', 'Oração')";

        vCriaTabelas.add(insereProgramacao2);

        String insereProgramacao3 = "INSERT INTO PROGRAMACAO (HORARIOS_PROG, DESCRICAO_PROG, DIASEMANA_PROG) VALUES "
                + "('10:00 `as 17:00', 'De segunda a sexta', 'Love Time')";

        vCriaTabelas.add(insereProgramacao3);


        String insereProgramacao4 = "INSERT INTO PROGRAMACAO (HORARIOS_PROG, DESCRICAO_PROG, DIASEMANA_PROG) VALUES "
                + "('06:00 `as 9:20', 'Sábado', 'Sertanejo')";

        vCriaTabelas.add(insereProgramacao4);


        String insereProgramacao5 = "INSERT INTO PROGRAMACAO (HORARIOS_PROG, DESCRICAO_PROG, DIASEMANA_PROG) VALUES "
                + "('09:20 `as 11:00', 'Sábado', 'Pagode')";

        vCriaTabelas.add(insereProgramacao5);


        String criatabelaSituacaoRadio = "CREATE TABLE RADIOSERVICE (ID_PROGRAMACAO INTEGER NOT NULL PRIMARY KEY,"
                + " SITU_RADIOSERVICE CHAR(1))";

        vCriaTabelas.add(criatabelaSituacaoRadio);

        String insertintoRADIOSERVICE = "INSERT INTO RADIOSERVICE (SITU_RADIOSERVICE) VALUES ('N')";

        vCriaTabelas.add(insertintoRADIOSERVICE);


    }
}
