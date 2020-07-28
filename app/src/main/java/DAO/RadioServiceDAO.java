package DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.sql.SQLDataException;
import java.util.ArrayList;

import Serializable.RadioServiceSerializable;
import Utils.DB;

/**
 * Created by TI on 30/04/2017.
 */

public class RadioServiceDAO {

    private DB db;
    private SQLiteDatabase database;


    public RadioServiceDAO(Context ctx) {

        this.db = new DB(ctx);
        this.database = db.getWritableDatabase();
    }

    public ArrayList<RadioServiceSerializable> getRadio() {

        try {

            String sql = "SELECT * FROM RADIOSERVICE";
            return cursorForList(database.rawQuery(sql, null));

        } catch (Exception e) {

            throw e;

        } finally {

            if (database != null) {

                database.close();
                db.close();
            }
        }

    }

    private ArrayList<RadioServiceSerializable> cursorForList(Cursor c) {

        Log.d("Radio - ProgramacaoDAO", "cursorForList");
        Log.d("Radio - ProgramacaoDao", "cursorForList - Qtde Registros: " + c.getCount());

        ArrayList<RadioServiceSerializable> aRadio = new ArrayList<>();

        try {

            if (c.moveToFirst()) {

                do {

                    RadioServiceSerializable radio = new RadioServiceSerializable();

                    radio.setId_radioservice(c.getInt(c.getColumnIndex("ID_PROGRAMACAO")));
                    radio.setSitu_radioservice(c.getString(c.getColumnIndex("SITU_RADIOSERVICE")));

                    aRadio.add(radio);

                } while (c.moveToNext());

            }
            return aRadio;

        } catch (Exception e) {

            Log.e("Radio - ProgramacaoDao", "Erro em \"cursorForList\"!!! "
                    + e.toString());
            throw e;

        } finally {

            c.close();
        }
    }

    public void updateRadioService(RadioServiceSerializable radio) throws SQLDataException {

        ContentValues c;

        try {

            database.beginTransaction();

            c = new ContentValues();

            c.put("SITU_RADIOSERVICE", radio.getSitu_radioservice());

            database.update("radioservice", c, "ID_PROGRAMACAO = ?", new String[]{"" + radio.getId_radioservice()});

            database.setTransactionSuccessful();

        } catch (Exception e) {

            Log.e("RadioServiceDAO", "Erro ao atualizar status do serviço. " + e.getMessage());
            throw new SQLDataException("Erro ao atualizar situacao do radio " + e.toString());

        } finally {

            if (database != null) {

                if (database.inTransaction()) {

                    database.endTransaction();
                }

                database.close();
                db.close();
            }


        }


    }
}
