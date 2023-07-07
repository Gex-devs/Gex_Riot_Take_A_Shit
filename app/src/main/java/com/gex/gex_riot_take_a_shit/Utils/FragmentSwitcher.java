package com.gex.gex_riot_take_a_shit.Utils;

import static com.gex.gex_riot_take_a_shit.MainActivity.flutterFragment;
import static com.gex.gex_riot_take_a_shit.MainActivity.viewModel;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.gex.gex_riot_take_a_shit.Game_Status;
import com.gex.gex_riot_take_a_shit.fragments.gameFragments.fragment_improved_ingame;
import com.gex.gex_riot_take_a_shit.fragments.gameFragments.improved_Agent_sel_fragment;
import com.gex.gex_riot_take_a_shit.fragments.gameFragments.menuSelection;
import com.gex.gex_riot_take_a_shit.party;

import org.json.JSONException;

import java.io.IOException;

import io.flutter.embedding.android.FlutterFragment;

public class FragmentSwitcher {
    
    private static FragmentManager _fragmentManager;
    private static ViewGroup _container;
    
    public FragmentSwitcher(FragmentManager fragmentManager,ViewGroup container){
        _fragmentManager = fragmentManager;
        _container = container;
    }

    public static void Agent_Select_fragment(){
        Fragment fragment = new improved_Agent_sel_fragment();
        new FragmentTransactionTask(_fragmentManager, _container, fragment).execute();
    }
    public static void Qeue_Menu() throws JSONException, IOException {
        Fragment fragment = new menuSelection(viewModel);
        new FragmentTransactionTask(_fragmentManager, _container, fragment).execute();
    }
    public static void Game_Status_Fragment(){
        Fragment fragment = new Game_Status();
        new FragmentTransactionTask(_fragmentManager, _container, fragment).execute();
    }
    public  static void Store_Fragment(){
        if(flutterFragment == null){
            flutterFragment = FlutterFragment.createDefault();
        }
        new FragmentTransactionTask(_fragmentManager, _container, flutterFragment).execute();

    }
    public static void Game_Fragment(){
        Fragment fragment = new fragment_improved_ingame();
        new FragmentTransactionTask(_fragmentManager, _container, fragment).execute();
    }

    public static void party_fragment(){
        Fragment fragment = new party();
        new FragmentTransactionTask(_fragmentManager, _container, fragment).execute();
    }
}