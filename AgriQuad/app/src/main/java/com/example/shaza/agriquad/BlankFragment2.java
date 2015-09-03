package com.example.shaza.agriquad;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment2 extends Fragment implements SeekBar.OnSeekBarChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  //  private static final String ARG_PARAM1 = "param1";
   // private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SECTION_NUMBER = "section_number";
  int progressChanged=0;
    // TODO: Rename and change types of parameters


  //  private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment2 newInstance(int sectionNumber) {
        BlankFragment2 fragment = new BlankFragment2();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment ;
    }

    public BlankFragment2() {
        // Required empty public constructor
    }

  //  @Override
  //  public void onCreate(Bundle savedInstanceState) {
    //    super.onCreate(savedInstanceState);
    //    if (getArguments() != null) {
     //       mParam1 = getArguments().getString(ARG_PARAM1);
     //       mParam2 = getArguments().getString(ARG_PARAM2);
      //  }
   // }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_blank_fragment2, container, false);
        SeekBar seekBar =(SeekBar)v.findViewById(R.id.seekBar);
        return v;

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        progressChanged = progress;
        // setMax()
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // bluetooth code
        Toast.makeText(getActivity(), "Height is : " + progressChanged, Toast.LENGTH_SHORT).show();
    }

    // TODO: Rename method, update argument and hook method into UI event
   // public void onButtonPressed(Uri uri) {
    //    if (mListener != null) {
     //       mListener.onFragmentInteraction(uri);
      //  }
    //}

   // @Override
  //  public void onAttach(Activity activity) {
    //    super.onAttach(activity);
    //    try {
     //       mListener = (OnFragmentInteractionListener) activity;
     //   } catch (ClassCastException e) {
      //      throw new ClassCastException(activity.toString()
       //             + " must implement OnFragmentInteractionListener");
       // }
    //}

   // @Override
   // public void onDetach() {
    //    super.onDetach();
     //   mListener = null;
   // }

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
   // public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
     //   public void onFragmentInteraction(Uri uri);
   // }

}
