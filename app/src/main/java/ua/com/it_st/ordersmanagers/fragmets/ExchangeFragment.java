package ua.com.it_st.ordersmanagers.fragmets;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.utils.GetFileList;
import ua.com.it_st.ordersmanagers.utils.SocketUtils;

/**
 * Created by Gens on 19.07.2015.
 */
public class ExchangeFragment extends Fragment implements View.OnClickListener {

    private static final int FTP_Port = 21;
    TextView tvInfo;
    EditText editHost;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.exchange_layout, container,
                false);
        tvInfo = (TextView) rootView.findViewById(R.id.tvInfo);
        editHost = (EditText) rootView.findViewById(R.id.editHost);

        Button BHost = (Button) rootView.findViewById(R.id.button1);
        BHost.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(final View view) {
        // FtpTask ftpTask = new FtpTask();
        // ftpTask.execute();
        // FtpTask ftpTask = new FtpTask();
        GetFileList getFileList = new GetFileList();
        String[] args = new String[1];
        args[0] = "";
        getFileList.main(args);
    }


    class FtpTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            String output = "";
            String host = editHost.getText().toString();
            try {
                Socket socket = new Socket(host, FTP_Port);

                BufferedReader reader = SocketUtils.getReader(socket);

                List<String> result = new ArrayList<String>();
                String line = reader.readLine();
                result.add(line);

                if (line.startsWith("220-")) {
                    while ((line = reader.readLine()) != null) {
                        result.add(line);
                        if ((line.equals("220") || line.startsWith("220 "))) {
                            break;
                        }
                    }
                }

                output = makeOutputString(result);

                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace(); // view this in DDMS window
            } catch (IOException e) {
                e.printStackTrace(); // view this in DDMS window
            }
            return output;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            tvInfo.setText(result);
        }

        private String makeOutputString(List<String> result) {
            StringBuilder output = new StringBuilder();
            for (String s : result) {
                output.append(s + "\n");
            }
            return (output.toString());
        }
    }
}
