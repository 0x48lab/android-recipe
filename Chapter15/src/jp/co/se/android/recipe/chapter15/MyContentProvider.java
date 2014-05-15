package jp.co.se.android.recipe.chapter15;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

public class MyContentProvider extends ContentProvider {
    // �R���e���c�v���o�C�_�փA�N�Z�X����ۂɍ쐬�����Uri��Authority
    public static final String AUTHORITY = "jp.co.se.android.recipe.chapter15";
    // Authority���ӂ��߂�Uri�̕�����
    public static final String CONTENT_AUTHORITY = "content://" + AUTHORITY
            + "/";

    // �R�����g�e�[�u���փA�N�Z�X����Uri
    public static final Uri COMMENTS_CONTENT_URI = Uri.parse(CONTENT_AUTHORITY
            + MySQLiteOpenHelper.TABLE_COMMENTS);

    // �R�����g��Uri�̔��ʃR�[�h
    public static final int CODE_COMMENT = 0;
    // ID�w��ɂ��R�����g��Uri�̔��ʃR�[�h
    public static final int CODE_COMMENT_ID = 1;

    // �R���e���c�v���o�C�_���ŋ��ʂŎg�p����SQLiteOpenHelper�C���X�^���X
    private MySQLiteOpenHelper mDatabaseHelper = null;
    // Uri�̔��ʂŎg�p
    private UriMatcher mUriMatcher = null;

    @Override
    public boolean onCreate() {
        // Uri���ʂ̂���UriMatcher�𐶐�
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // �R�����g��Uri���ʂ̂��߂̃p�^�[����o�^
        mUriMatcher.addURI(AUTHORITY, MySQLiteOpenHelper.TABLE_COMMENTS,
                CODE_COMMENT);
        // ID�w��ɂ��R�����g��Uri���ʂ̂��߂̃p�^�[����o�^
        mUriMatcher.addURI(AUTHORITY, MySQLiteOpenHelper.TABLE_COMMENTS + "/#",
                CODE_COMMENT_ID);

        // SQLiteOpenHelper�C���X�^���X���擾
        mDatabaseHelper = new MySQLiteOpenHelper(getContext());

        return true;
    }

    @Override
    public String getType(Uri uri) {
        // �{�T���v���ł͕�����Uri�ɑΉ����Ă��Ȃ����ߖ�����
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        // ����query���쐬���邽��SQLiteQueryBuilder�𐶐�
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // UriMatcher��p����Uri���猟������f�[�^��ʂ𔻒�
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT:
            // �R�����g�ɑ΂���query�ݒ�
            queryBuilder.setTables(MySQLiteOpenHelper.TABLE_COMMENTS);
            break;
        case CODE_COMMENT_ID:
            // ID�w��̃R�����g�ɑ΂���query�ݒ�
            queryBuilder.setTables(MySQLiteOpenHelper.TABLE_COMMENTS);
            queryBuilder.appendWhere(BaseColumns._ID + "="
                    + uri.getLastPathSegment());
            break;
        default:
            // �\������Ă��Ȃ��f�[�^��ʂȂ̂ŗ�O�𔭐�
            throw new IllegalArgumentException("Unknown URI");
        }

        // query�����s���������ʂ�ԋp
        Cursor cursor = queryBuilder.query(
                mDatabaseHelper.getReadableDatabase(), projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // UriMatcher��p����Uri����폜����f�[�^��ʂ𔻒�
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // �R�����g�ɑ΂���delete�����s���폜����ԋp
            return delete(MySQLiteOpenHelper.TABLE_COMMENTS, selection,
                    selectionArgs);
        }
        case CODE_COMMENT_ID: {
            // Uri����ID���擾
            long id = ContentUris.parseId(uri);
            // ID�w��̃R�����g�ɑ΂���delete�����s���폜����ԋp
            return delete(MySQLiteOpenHelper.TABLE_COMMENTS, BaseColumns._ID
                    + " = ?", new String[] { Long.toString(id) });
        }
        }
        // �������Ȃ��̂łO��ԋp
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // UriMatcher��p����Uri����}������f�[�^��ʂ𔻒�
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // �R�����g�ɑ΂���insert�����s���A���ʂƂ��Ď擾����ID����Uri�𐶐����ԋp
            return ContentUris.withAppendedId(COMMENTS_CONTENT_URI,
                    insert(MySQLiteOpenHelper.TABLE_COMMENTS, values));
        }
        }
        // �������Ȃ��̂�null��ԋp
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // UriMatcher��p����Uri����}������f�[�^��ʂ𔻒�
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // �R�����g�ɑ΂���update�����s���@�X�V����ԋp
            return update(MySQLiteOpenHelper.TABLE_COMMENTS, values, selection,
                    selectionArgs);
        }
        case CODE_COMMENT_ID: {
            long id = ContentUris.parseId(uri);
            // ID�w��̃R�����g�ɑ΂���update�����s���X�V����ԋp
            return update(MySQLiteOpenHelper.TABLE_COMMENTS, values,
                    BaseColumns._ID + " = ?",
                    new String[] { Long.toString(id) });
        }
        }
        // �������Ȃ��̂łO��ԋp
        return 0;
    }

    public int delete(String table, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db.delete(table, selection, selectionArgs);
    }

    private long insert(String table, ContentValues values) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db.insert(table, null, values);
    }

    private int update(String table, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db.update(table, values, selection, selectionArgs);
    }

}
