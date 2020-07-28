package DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import Serializable.ProgramacaoSerial;
import Utils.DB;

/**
 * Created by TI on 23/03/2016.
 */
public class ProgramacaoDAO {

    private DB dh;
    private SQLiteDatabase bancoDados;

    public ProgramacaoDAO(Context ctx) {

        this.dh = new DB(ctx);
        this.bancoDados = dh.getWritableDatabase();
    }

    public ArrayList<ProgramacaoSerial> buscarProgramacao() {

        try {

            String sql = " select * from programacao p";
            return cursorForList(bancoDados.rawQuery(sql, null));

        } catch (Exception e) {

            throw e;

        } finally {

            bancoDados.close();
            dh.close();

        }


    }

    private ArrayList<ProgramacaoSerial> cursorForList(Cursor c) {

        Log.d("Radio - ProgramacaoDAO", "cursorForList");
        Log.d("Radio - ProgramacaoDao", "cursorForList - Qtde Registros: " + c.getCount());

        ArrayList<ProgramacaoSerial> aProg = new ArrayList<ProgramacaoSerial>();

        try {

            if (c.moveToFirst()) {

                do {


                    ProgramacaoSerial programacao = new ProgramacaoSerial();

                    programacao.setId_programacao(c.getInt(c.getColumnIndex("ID_PROGRAMACAO")));
                    programacao.setHorarios(c.getString(c.getColumnIndex("HORARIOS_PROG")));
                    programacao.setDescricaoPrograma(c.getString(c.getColumnIndex("DESCRICAO_PROG")));
                    programacao.setDiaprograma(c.getString(c.getColumnIndex("DIASEMANA_PROG")));


                    aProg.add(programacao);
                } while (c.moveToNext());

            }

            return aProg;

        } catch (Exception e) {

            Log.e("Radio - ProgramacaoDao", "Erro em \"cursorForList\"!!! "
                    + e.toString());

            throw e;


        } finally {

            c.close();
        }


    }

}
