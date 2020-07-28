package Utils;

import android.util.Log;

import java.util.Vector;

/**
 * Created by TI on 30/04/2017.
 */

public class UpdateTable {

    private Vector<String> vUpdateTable;
    private int versaoBanco;

    public UpdateTable(int versionDB) {

        Log.d("RadioServiceDAO", "UpdateTable - Atualiza tabelas");
        /*Cria um novo vetor*/
        vUpdateTable = new Vector<>();
        /*Pega a versao do banco*/
        this.versaoBanco = versionDB;
        /*Chama o metodo para atualizar as tabelas do banco*/
        updateTables();
    }

    public Vector<String> atualizaTabelas() {

        return this.vUpdateTable;
    }

    public void updateTables() {

        if (versaoBanco == 7) {

            String addTableRADIOSERVICE = "CREATE TABLE IF NOT EXISTS RADIOSERVICE (ID_PROGRAMACAO INTEGER NOT NULL PRIMARY KEY,"
                    + " SITU_RADIOSERVICE CHAR(1))";

            vUpdateTable.add(addTableRADIOSERVICE);


            String insertRadioService = "INSERT INTO RADIOSERVICE (SITU_RADIOSERVICE) VALUES ('N')";

            vUpdateTable.add(insertRadioService);

        }

    }
}
