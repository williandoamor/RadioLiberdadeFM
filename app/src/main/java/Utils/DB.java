package Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by TI on 23/03/2016.
 */
public class DB extends SQLiteOpenHelper {


    private static String NOME_BANCO = "RADIOLIBERDADEFM";

    private static int VERSAO_BANCO = 7;
    public static SQLiteDatabase bancoDados;

    public DB(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("LoadTI - DBHelper", "Metodo onCreate");

        try {

            // Instancia a classe tabela e cria um objeto dessa classe
            CriaTabela tab = new CriaTabela(VERSAO_BANCO);

            // cria uma variavel int i e verifica, enquanto i for menor que o
            // metodo para cariar
            // as tabelas da classe cria_tabelas cria tabelas e incrementa a
            // variavel i

            for (int i = 0; i < tab.pegaTabelas().size(); i++) {

                db.execSQL(tab.pegaTabelas().get(i));

            }

            Log.d("LoadTI - DBHelper",
                    "Metodo onCreate. As tabelas foram criadas com sucesso");

        } catch (Exception e) {

            Log.e("LoadTI - DBHelper", "Metodo onCreate. Falha " + e.toString()
                    + " ao criar as tabelas do banco", e);

            throw e;


        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < newVersion) {

            Log.d("DB", "onUpgradeDB - Versao antiga do banco " + oldVersion + "nova versao do banco " + newVersion);

            try {

                UpdateTable update = new UpdateTable(VERSAO_BANCO);

                for (int x = 0; x < update.atualizaTabelas().size(); x++) {

                    db.execSQL(update.atualizaTabelas().get(x));
                }

            } catch (Exception e) {

                Log.e("DB", "onUpdateDB - Erro ao atualizar o banco de dados " + e.toString());
                throw e;
            }
        }

    }
}
