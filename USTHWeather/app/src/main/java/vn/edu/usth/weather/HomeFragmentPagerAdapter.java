package vn.edu.usth.weather;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT =3;
    private String titles[]= new String[] {"Hanoi", "Paris","Toulouse"};
    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public int getCount(){
        return PAGE_COUNT;
    }
    @NonNull
    @Override
    public Fragment getItem(int page){
        switch (page) {
            case 0: return new WeatherAndForecastFragment();
            case 1: return new WeatherAndForecastFragment();
            case 2: return new WeatherAndForecastFragment();
        }
        return new WeatherAndForecastFragment(); //failsafe
    }
    @Override
    public CharSequence getPageTitle(int page){
        return titles[page];
    }
}
