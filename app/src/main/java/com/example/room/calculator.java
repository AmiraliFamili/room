package com.example.room;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.room.Gallery.Gallery;
import com.example.room.pass.passwordGN;
import com.google.android.material.navigation.NavigationView;

/**
 * @see calculator
 *
 *      - Class calculator is a single class responsible for the entire calculator backend operations.
 *
 * @Note This class has no other helper class
 *
 * @author Amirali Famili
 */
public class calculator extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bdot, bpi, bequal, bplus, bmin, bmul, bdiv, binv, bsquare, bfact, bln, blog,bsqrt, btan, bcos, bsin, bb1, bb2, bc, bac, tvsec, tvmain;
    final String pi = "3.141592653589793238462643383279502884197";
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        initialise();// initialise elements

        // set the navigation menu on the top left
        NavigationView navigationView = findViewById(R.id.notes_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.notes_main_page);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        // setting on click listener for all elements inside the calculator's layout
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"1");
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"2");
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"3");
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"4");
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"5");
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"6");
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"7");
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"8");
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"9");
            }
        });
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"0");
            }
        });
        bdot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+".");
            }
        });
        bac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText("");
            }
        });
        bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   String val = tvmain.getText().toString();
                   val = val.substring(0, val.length() -1);
                   tvmain.setText(val);
               } catch (Exception e){
                   tvmain.setText("");
               }
            }
        });
        bplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"+");
            }
        });
        bmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"-");
            }
        });
        bmul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"×");
            }
        });
        bdiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"÷");
            }
        });
        bsqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              try {
                  String val = tvmain.getText().toString();
                  double r = Math.sqrt(Double.parseDouble(val));
                  tvmain.setText(String.valueOf(r));
              } catch (Exception e){
                  tvmain.setText("Not sqrtable...");
              }
            }
        });
        bb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"(");
            }
        });
        bb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+")");
            }
        });
        bpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvsec.setText(bpi.getText());
                tvmain.setText(tvmain.getText() + pi);
            }
        });
        bsin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"sin");
            }
        });
        bcos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"cos");
            }
        });
        btan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"tan");
            }
        });
        binv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"^"+"(-1)");
            }
        });
        bfact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int val = Integer.parseInt(tvmain.getText().toString());
                    int fact = factorial(val);
                    tvmain.setText(String.valueOf(fact));
                    tvsec.setText(val+"!");
                }catch (Exception e){
                    tvmain.setText("ineffable");
                }
            }
        });
        bsquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              try {
                  double d = Double.parseDouble(tvmain.getText().toString());
                  double square = d * d;
                  tvmain.setText(String.valueOf(square));
                  tvsec.setText(d+"²");
              } catch (Exception e){
                  tvmain.setText("Bad Math...");
              }
            }
        });

        bln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"ln");
            }
        });
        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmain.setText(tvmain.getText()+"log");
            }
        });
        bequal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String val = tvmain.getText().toString();
                    String replacedstr = val.replace('÷', '/').replace('×', '*');
                    double result = eval(replacedstr);
                    tvmain.setText(String.valueOf(result));
                    tvsec.setText(val);
                } catch (Exception e){
                    tvmain.setText("Bad Math...");
                    Log.e("Calculator", "Error evaluating expression", e);
                }
            }
        });
    }

    /**
     *
     *      - This method is calculates the factorial of a given integer as input.
     *
     *
     * @param n : an integer
     *
     *
     * @return n!
     */
    private int factorial(int n) {
        if (n < 0) {
            throw new RuntimeException("Undefined for : " + n);
        }
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }


    /**
     *
     *      - Since when the user clicks on a function on calculator like log, the text "log" will be added the to string
     *      This method will parse that string and extract the meaningful symbols form the text and produce results from them.
     *
     *
     * @param  str : input from the calculator
     *
     *
     * @return val : the returned value is a double which is the answer to the math problem
     */
    private static double eval(final String str){
        return new Object(){
            int pos = -1, ch;

            void nextChar(){
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat (int charToEat){
                while (ch == ' ') nextChar();
                if (ch == charToEat){
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse(){
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) {
                    throw new RuntimeException("Unexpected : " + (char) ch);
                }return x;
            }

            double parseExpression(){
                double x = parseTerm();
                for (;;){
                    if (eat('+')) {x += parseTerm();}
                    else if (eat('-')){ x += parseTerm();}
                    else {return x;}
                }
            }

            double parseTerm(){
                double x = parseFactor();
                for (;;){
                    if (eat('*')){ x *= parseFactor();}
                    else if (eat('/')){ x /= parseFactor();}
                    else {return x;}
                }
            }

            double parseFactor(){
                if (eat('+')){ return parseFactor();}
                else if (eat('-')) {return -parseFactor();}

                double x;
                int startPos = this.pos;
                if (eat('(')){
                    x = parseExpression();
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z'){
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) {x = Math.sqrt(x);}
                    else if (func.equals("sin")){ x = Math.sin(Math.toRadians(x));}
                    else if (func.equals("cos")){ x = Math.cos(Math.toRadians(x));}
                    else if (func.equals("tan")){ x = Math.tan(Math.toRadians(x));}
                    else if (func.equals("log")){ x = Math.log10(x);}
                    else if (func.equals("ln")){ x = Math.log(x);}
                    else {throw new RuntimeException("Unknown Function : " + func);}
                } else {
                    throw new RuntimeException("Unknown Operator : " + (char)ch);
                }
                if (eat('^')) {x = Math.pow(x, parseFactor());}
                return x;
            }
        }.parse();
    }

    /**
     *
     *      - Initialise all elements.
     */
    private void initialise(){
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        b5 = findViewById(R.id.b5);
        b6 = findViewById(R.id.b6);
        b7 = findViewById(R.id.b7);
        b8 = findViewById(R.id.b8);
        b9 = findViewById(R.id.b9);
        b0 = findViewById(R.id.b0);
        bdot = findViewById(R.id.bdot);
        bpi = findViewById(R.id.bpi);
        bequal = findViewById(R.id.bequal);
        bplus = findViewById(R.id.bplus);
        bmin = findViewById(R.id.bmin);
        bmul = findViewById(R.id.bmul);
        bdiv = findViewById(R.id.bdiv);
        binv = findViewById(R.id.binv);
        bsquare = findViewById(R.id.bsquare);
        bfact = findViewById(R.id.bfact);
        bln = findViewById(R.id.bln);
        blog = findViewById(R.id.blog);
        btan = findViewById(R.id.btan);
        bcos = findViewById(R.id.bcos);
        bsqrt = findViewById(R.id.bsqrt);
        bsin = findViewById(R.id.bsin);
        bb1 = findViewById(R.id.bb1);
        bb2 = findViewById(R.id.bb2);
        bc = findViewById(R.id.bc);
        bac = findViewById(R.id.bac);
        tvsec = findViewById(R.id.tvsec);
        tvmain = findViewById(R.id.tvmain);
    }


    /**
     *      - This method is responsible for redirecting user to the correct activity when they click on the items listed on the navigation menu.
     *
     * @param item : id of the item user clicked on it
     *
     * @return true if item exists, false if it doesn't
     */
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation item clicks here
        int id = item.getItemId();
        if (id == R.id.homeInNav){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.noteInNav){
            Intent intent = new Intent(this, Notes.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.musicInNav){
            Intent intent = new Intent(this, music_main.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.passwordInNav){
            Intent intent = new Intent(this, passwordGN.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.galleryInNav){
            Intent intent = new Intent(this, Gallery.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.calculatorInNav){
            Intent intent = new Intent(this, calculator.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.aboutInNav){
            Intent intent = new Intent(this, aboutUs.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.shareInNav){
            Intent intent = new Intent(this, shareUs.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.rateInNav){
            Intent intent = new Intent(this, rateUs.class);
            startActivity(intent);
            return true;
        } else {
            return false;
        }
    }
}