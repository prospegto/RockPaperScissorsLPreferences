package es.iesnervion.android.ignacio.pptconpreferencias;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PiedraPapelTijera extends Activity {

	Button btnPiedra, btnPapel, btnTijera, btnEstadisticas, btnReset;
	TextView label, lblIntentos;
	Boolean gana;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_piedra_papel_tijera);

		label = (TextView) findViewById(R.id.txtResultado);
		lblIntentos = (TextView) findViewById(R.id.lblIntentos);
		btnPiedra = (Button) findViewById(R.id.btnPiedra);
		btnPapel = (Button) findViewById(R.id.btnPapel);
		btnTijera = (Button) findViewById(R.id.btnTijera);
		btnEstadisticas = (Button) findViewById(R.id.btnConsultar);
		btnReset = (Button) findViewById(R.id.btnReset);
		btnEstadisticas.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				recuperarPreferenicas();
			}
		});
		
		btnReset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetearPreferencias();
			}
		});

		btnPiedra.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				gana = jugar("piedra");
				int intentos = recuperarIntentos(gana);
				guardarPreferenciaInt("ppt.intentos", intentos);
			}
		});

		btnPapel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gana = jugar("papel");
				int intentos = recuperarIntentos(gana);
				guardarPreferenciaInt("ppt.intentos", intentos);
			}
		});

		btnTijera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gana = jugar("tijera");
				int intentos = recuperarIntentos(gana);
				guardarPreferenciaInt("ppt.intentos", intentos);
			}
		});


	}
	
	protected void resetearPreferencias() {
		// TODO Auto-generated method stub
		guardarPreferenciaInt("ppt.intentos", 0);
		guardarPreferenciaInt("ppt.intentosPerdidos", 0);
		guardarPreferenciaInt("ppt.partidasPerdidas", 0);
		guardarPreferenciaInt("ppt.partidasGanadas", 0);
		lblIntentos.setText("Intento 0 de 3");
		label.setText("");
	}

	private  int contadorPierde() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = getSharedPreferences("ppt.Preferencias",Context.MODE_PRIVATE);	
		String partidasPerdidas =  String.valueOf(prefs.getInt("ppt.partidasPerdidas", 0));
		int contPartidasP = Integer.parseInt(partidasPerdidas);
		contPartidasP++;
		return contPartidasP;
	}
	
	private  int contadorGana() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = getSharedPreferences("ppt.Preferencias",Context.MODE_PRIVATE);	
		String partidasGanadas =  String.valueOf(prefs.getInt("ppt.partidasGanadas", 0));
		int contPartidasG = Integer.parseInt(partidasGanadas);
		contPartidasG++;
		return contPartidasG;
	}
	
	private  int contadorIntentosPerdidos() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = getSharedPreferences("ppt.Preferencias",Context.MODE_PRIVATE);	
		String intentosPerdidos =  String.valueOf(prefs.getInt("ppt.intentosPerdidos", 0));
		int contIntentosPerdidos = Integer.parseInt(intentosPerdidos);
		contIntentosPerdidos++;
		return contIntentosPerdidos;
	}
	
	private int recuperarIntentos(Boolean gana) {
		// TODO Auto-generated method stub
		//SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences prefs = getSharedPreferences("ppt.Preferencias",Context.MODE_PRIVATE);
		
		String intentosPerdidos =  String.valueOf(prefs.getInt("ppt.intentosPerdidos", 0));	
		int contIntentosPerdidos = Integer.parseInt(intentosPerdidos);
		
		if(!gana){
			int cintentosPerdidos = contadorIntentosPerdidos();
			guardarPreferenciaInt("ppt.intentosPerdidos", cintentosPerdidos);
		}
		
		String intentos =  String.valueOf(prefs.getInt("ppt.intentos", 0));	
		int contIntentos = Integer.parseInt(intentos);
		if(contIntentos<3){
			contIntentos++;
		}else{
			contIntentos = 1;
			if(contIntentosPerdidos>=2){
				int partidasPerdidas = contadorPierde();
				guardarPreferenciaInt("ppt.partidasPerdidas", partidasPerdidas);
			}else{
				int partidasGanadas = contadorGana();
				guardarPreferenciaInt("ppt.partidasGanadas", partidasGanadas);
			}
			guardarPreferenciaInt("ppt.intentosPerdidos", 0);
		}

		lblIntentos.setText("Intento "+String.valueOf(contIntentos)+" de 3");
		return contIntentos;
	}

	private void recuperarPreferenicas() {
		// TODO Auto-generated method stub
		//SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences prefs = getSharedPreferences("ppt.Preferencias",Context.MODE_PRIVATE);
		String partidasPerdidas =  String.valueOf(prefs.getInt("ppt.partidasPerdidas", 0));
		String intentos =  String.valueOf(prefs.getInt("ppt.intentos", 0));
		String intentosPerdidos =  String.valueOf(prefs.getInt("ppt.intentosPerdidos", 0));
		String partidasGanadas =  String.valueOf(prefs.getInt("ppt.partidasGanadas", 0));
		label.setText("Ha perdido "+partidasPerdidas+ " partidas\nHa ganado "+partidasGanadas+ " partidas\nLleva "+intentos+" intentos de los cuales ha perdido "+intentosPerdidos);
	}

	private void guardarPreferenciaInt(String key, int i) {
		//SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences prefs = getSharedPreferences("ppt.Preferencias",Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt(key, i);
		editor.commit();
	}

	private boolean jugar(String eleccion) {

		boolean gana = false;
		int num = (int) (Math.random() * 3 + 1);  
		//int num = 2;
		
		if(eleccion == "piedra") {
			if (num == 1){
				jugar("piedra");
			}else if (num == 2){
				label.setText("Has perdido");			
				gana = false;
			}else if (num == 3){
				label.setText("Has ganado");			
				gana = true;
			}
		}
		else if (eleccion == "papel") {
			if (num == 2){
				jugar("papel");
			}else if (num == 3){
				label.setText("Has perdido");			
				gana = false;
			}else if (num == 1){
				label.setText("Has ganado");			
				gana = true;
			}
		}
		else if (eleccion == "tijera") {
			if (num == 3){
				jugar("tijera");
			}else if (num == 1){
				label.setText("Has perdido");			
				gana = false;
			}else if (num == 2){
				label.setText("Has ganado");			
				gana = true;
			}
		}
		return gana;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.piedra_papel_tijera, menu);
		return true;
	}

}
