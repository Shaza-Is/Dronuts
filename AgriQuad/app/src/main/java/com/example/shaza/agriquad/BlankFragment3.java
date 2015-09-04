package com.example.shaza.agriquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment3 extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
 //   private static final String ARG_PARAM1 = "param1";
  //  private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
   // private String mParam1;
   // private String mParam2;
    private static final String ARG_SECTION_NUMBER = "section_number";
   // private OnFragmentInteractionListener mListener;




    public void map(View v){
       Intent intent = new Intent(getActivity(), MapsActivity.class);
       startActivity(intent);

   }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment3 newInstance(int sectionNumber) {
        BlankFragment3 fragment = new BlankFragment3();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;

    }

    public BlankFragment3() {
        // Required empty public constructor
    }


  //  @Override
//    public void onCreate(Bundle savedInstanceState) {
 //       super.onCreate(savedInstanceState);
 //       if (getArguments() != null) {
 //           mParam1 = getArguments().getString(ARG_PARAM1);
 //           mParam2 = getArguments().getString(ARG_PARAM2);
//        }
 //   }

 //   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_blank_fragment3, container, false);
        View view = inflater.inflate(R.layout.fragment_blank_fragment3, container, false);
        Spinner spinner = (Spinner)view.findViewById(R.id.spinner);
        // spinner.setOnItemSelectedListener(this);
        //  ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(rootView.getContext(),R.array.planets_array,android.R.layout.simple_spinner_dropdown_item) ;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.planets_array, android.R.layout.simple_spinner_dropdown_item); // Create an ArrayAdapter using the string array and a default spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void onCheckboxClicked(View hi) {

        // Is the view now checked?
        boolean checked = ((CheckBox) hi).isChecked();

        // Check which checkbox was clicked
        switch (hi.getId()) {
            case R.id.captures:
                if (checked) {
                    // Put some meat on the sandwich
                } else

                {// Remove the meat
                }
            case R.id.height:
                if (checked) {
                    // Cheese me
                } else {
                    // I'm lactose intolerant
                }
            case R.id.checkBox3:
                if (checked) {
                    //khk
                }else {
                    //bnjn
                }
                // TODO: Veggie sandwich


        }
    }

    // TODO: Rename method, update argument and hook method into UI event
 //   public void onButtonPressed(Uri uri) {
 //       if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

 //   @Override
 //   public void onAttach(Activity activity) {
 //       super.onAttach(activity);
 //       try {
 //           mListener = (OnFragmentInteractionListener) activity;
   //     } catch (ClassCastException e) {
   //         throw new ClassCastException(activity.toString()
    //                + " must implement OnFragmentInteractionListener");
   //     }
 //   }

  //  @Override
  //  public void onDetach() {
    //    super.onDetach();
  //      mListener = null;
  //  }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
 //   public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
 //       public void onFragmentInteraction(Uri uri);
 //   }

  /*  public void toMap(View v){
        Intent intent = new Intent(getActivity(), MapsActivity.class);

        startActivity(intent);

    }*/
}
