package ua.com.it_st.ordersmanagers.fragmets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.it_st.ordersmanagers.R;

/**
 * Created by Gens on 30.07.2015.
 */
public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.main_list, container,
                false);

//        String[] strings = new String[]{"One", "Two", "Three"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_list_item_1, android.R.id.text1, strings);
//
////        // настраиваем список
//        ListView lvMain = (ListView) rootView.findViewById(R.id.lvMain);
//        lvMain.setAdapter(adapter);
//
//        MainActivity mainActivity = (MainActivity) getActivity();
//        mainActivity.getToolbar().setMinimumHeight(300);
        return rootView;
    }

}
