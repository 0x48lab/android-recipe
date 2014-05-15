package jp.co.se.android.recipe.chapter02;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

public class Ch0203 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0203_main);

        // �e���X�g
        ArrayList<HashMap<String, String>> groupData = new ArrayList<HashMap<String, String>>();
        // �q���X�g
        ArrayList<ArrayList<HashMap<String, String>>> childData = new ArrayList<ArrayList<HashMap<String, String>>>();

        // �e���X�g�ɗv�f��ǉ�
        HashMap<String, String> groupA = new HashMap<String, String>();
        groupA.put("group", "����");
        HashMap<String, String> groupB = new HashMap<String, String>();
        groupB.put("group", "�Ƃ�");

        groupData.add(groupA);
        groupData.add(groupB);

        // �q���X�g�ɗv�f��ǉ�(1)
        ArrayList<HashMap<String, String>> childListA = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> childAA = new HashMap<String, String>();
        childAA.put("group", "����");
        childAA.put("name", "�j�z���U��");
        HashMap<String, String> childAB = new HashMap<String, String>();
        childAB.put("group", "����");
        childAB.put("name", "�e�i�K�U��");
        HashMap<String, String> childAC = new HashMap<String, String>();
        childAC.put("group", "����");
        childAC.put("name", "���K�l�U��");

        childListA.add(childAA);
        childListA.add(childAB);
        childListA.add(childAC);

        childData.add(childListA);

        // �q���X�g�ɗv�f��ǉ�(2)
        ArrayList<HashMap<String, String>> childListB = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> childBA = new HashMap<String, String>();
        childBA.put("group", "�Ƃ�");
        childBA.put("name", "�j���g��");
        HashMap<String, String> childBB = new HashMap<String, String>();
        childBB.put("group", "�Ƃ�");
        childBB.put("name", "�X�Y��");

        childListB.add(childBA);
        childListB.add(childBB);

        childData.add(childListB);

        // �e���X�g�A�q���X�g���܂�Adapter�𐶐�
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                getApplicationContext(), groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { "group" }, new int[] { android.R.id.text1 },
                childData, android.R.layout.simple_expandable_list_item_2,
                new String[] { "name", "group" }, new int[] {
                        android.R.id.text1, android.R.id.text2 });

        // ExpandableListView��Adapter���Z�b�g
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.ExpandablelistView);
        listView.setAdapter(adapter);
    }
}