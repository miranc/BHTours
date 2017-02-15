package info.bhtours.bhtours.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import info.bhtours.bhtours.R;
import info.bhtours.bhtours.util.MailSender;


public class ContactFragment extends Fragment {
    private String mailsmtpserver;
    private String mailusername;
    private String mailpassword;
    private String mailrecipient;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            mailsmtpserver = getArguments().getString("mailsmtpserver");
            mailusername = getArguments().getString("mailusername");
            mailpassword = getArguments().getString("mailpassword");
            mailrecipient = getArguments().getString("mailrecipient");
        }
        final View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        Button button = (Button) rootView.findViewById(R.id.ButtonSend);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    //String senderName = ((TextView)rootView.findViewById(R.id.tvMailSenderName)).getText().toString();
                    String senderEmail = ((TextView)rootView.findViewById(R.id.tvMailSenderEmail)).getText().toString();
                    String body = ((TextView)rootView.findViewById(R.id.tvMailBody)).getText().toString();
                    if(!mailusername.equals("") && !mailusername.equals("USERNAME") && !mailpassword.equals("") && !mailpassword.equals("PASSWORD")) {
                        MailSender sender = new MailSender(mailsmtpserver, mailusername, mailpassword);
                        sender.sendMail(getString(R.string.mailSubject),
                                body,
                                senderEmail,
                                mailrecipient);
                    }
                    Toast.makeText(getActivity(),getString(R.string.mailSendSuccess),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
