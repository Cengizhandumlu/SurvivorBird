package com.example.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.awt.image.CropImageFilter;
import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {

	SpriteBatch batch; //sprite objeleri cizmemiz icin gereken sınıf.
	Texture background; //genelde objenin imajını aldıgını patern
	Texture bird; //kusumuzu cizmek icin ekledik
	Texture bee1;
	Texture bee2;
	Texture bee3;
	float birdX=0;
	float birdY=0;
	int gameState=0;//oyun baslamamısken oyun state'i sıfır olsun.
	float velocity=0;//hız
	float gravtiy=0.1f; //yer cekimi

	//arıları dongude yapabilmemiz icin dizi kullanacagız.
	int numberOfEnemies=4;//arıların set sayısı.

	float[] enemyX=new float[numberOfEnemies];
	float distance=0; //arı setleri icin mesafe olusturmak icin tanımlama yaptık.
	float enemyVelocity=2; //enemy icin bir hız olusturduk.

	float[] enemyOffset1=new float[numberOfEnemies];//bir degisken surekli degisecekse
	float[] enemyOffset2=new float[numberOfEnemies];//bir degisken surekli degisecekse
	float[] enemyOffset3=new float[numberOfEnemies];//bir degisken surekli degisecekse

	Random random;//offset icin bir random sınıfından random degisken olusturduk.

	//carpısmaları anlamak ıcın bir circle tanımlıyoruz.
	Circle birdCircle;

	//circle nasıl oldugunu gormek icin renklendirme yapacagız.
	ShapeRenderer shapeRenderer;

	Circle[] enemyCircles1;
	Circle[] enemyCircles2;
	Circle[] enemyCircles3;

	//skorlama islemi icin bir int deger olusturuyoruz.
	int score=0;
	int scoreEnemy=0;

	//score yazdırmak icin bir font yaratıyoruz.
	BitmapFont font;
	BitmapFont font2;

	@Override
	public void create () {//uygulama ilk acıldıgında calısacak kısım.
		//initialize kısmını burada yapmamız gerekiyor.
		batch=new SpriteBatch();
		background=new Texture("background.png");
		bird=new Texture("bird.png");
		bee1=new Texture("bee.png");
		bee2=new Texture("bee.png");
		bee3=new Texture("bee.png");

		distance=Gdx.graphics.getWidth() / 2;
		random=new Random();//random initialize edildi.


		birdX=Gdx.graphics.getWidth()/2-bird.getHeight()/2;//x ve y yi bir degiskene atadık.
		birdY=Gdx.graphics.getHeight()/3;

		birdCircle=new Circle();
		enemyCircles1=new Circle[numberOfEnemies];
		enemyCircles2=new Circle[numberOfEnemies];
		enemyCircles3=new Circle[numberOfEnemies];

		//shapeRenderer=new ShapeRenderer();
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(4);//font olusturduk ve ekranda gostermek icin yazdık.

		font2=new BitmapFont();
		font2.setColor(Color.WHITE);
		font2.getData().setScale(5);

		for (int i=0;i<numberOfEnemies;i++){

			enemyOffset1[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);//bize bir yuzde verecek.
			enemyOffset2[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);//bize bir yuzde verecek.
			enemyOffset3[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);//bize bir yuzde verecek.



			enemyX[i]=Gdx.graphics.getWidth() - bee1.getWidth()/2 + i*distance;//arılar arası mesafe oluyor

			enemyCircles1[i]=new Circle();
			enemyCircles2[i]=new Circle();
			enemyCircles3[i]=new Circle();

		}


	}

	@Override
	public void render () {//oyun devam ettigi surece cagrılan method.

		batch.begin();
		//arasına ne cizecegimizi yazıyoruz.
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());//x ve y ekranın neresine konumlandıracagımızı soyluyoruz.


		//* if (Gdx.input.justTouched()){//kullanıcı dokundugunda ne olacak ?
			//kusa her tıklandıgında y ekseninde bir degisim elde etmek isteyecegiz.

			//gameState=1;//basladı ve 1 oldu.}

		if (gameState==1){//kendi yer çekimimizi olusturduk.


			if (enemyX[scoreEnemy]<Gdx.graphics.getWidth()/2-bird.getHeight()/2){

				score++;

				if (scoreEnemy<numberOfEnemies-1){
					scoreEnemy++;
				}else {
					scoreEnemy=0;
				}
			}



			if (Gdx.input.justTouched()){//kullanıcı dokundugunda ne olacak ?

				velocity=-8;//basladı ve 1 oldu.

			}

			for (int i=0;i<numberOfEnemies;i++){

				if (enemyX[i] < Gdx.graphics.getWidth()/15){//enemyX[i] sıfırdan kucukse gibi bir ifade kullandık kusuunda uzunlugunu katarak.

					//arının kucuklugunun altına indiyse eger
					enemyX[i]=enemyX[i]+numberOfEnemies*distance;

					enemyOffset1[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);//bize bir yuzde verecek.
					enemyOffset2[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);//bize bir yuzde verecek.
					enemyOffset3[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);//bize bir yuzde verecek.





				}else{//degilse bunları yapmaya devam et diyecegiz.
					enemyX[i]=enemyX[i]-enemyVelocity;//her olusturdugumuz bir arı seti icin enemyVelocity tanımladık ve cıkardık.

				}


				//oyun basladıgı gibi arıların geldigini gormek isteyecegiz.
				batch.draw(bee1,enemyX[i],Gdx.graphics.getHeight()/2+enemyOffset1[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
				batch.draw(bee2,enemyX[i],Gdx.graphics.getHeight()/2+enemyOffset2[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
				batch.draw(bee3,enemyX[i],Gdx.graphics.getHeight()/2+enemyOffset3[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);

				enemyCircles1[i]=new Circle(enemyX[i]+  +Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset1[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
				enemyCircles2[i]=new Circle(enemyX[i]+  +Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset2[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
				enemyCircles3[i]=new Circle(enemyX[i]+  +Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset3[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);

			}






			if (birdY>0){//yer altına inme demek istiyoruz.
				velocity=velocity+gravtiy;
				birdY=birdY-velocity;
			}else{
				gameState=2;
			}


		}else if(gameState==0){//sıfırsa dursun kus , oyun baslamadıysa eger.

			if (Gdx.input.justTouched()){//kullanıcı dokundugunda ne olacak ?

				gameState=1;//basladı ve 1 oldu.

			}

		}else if(gameState==2){//oyun bittiyse ve kullanıcı tekrar tıkladıysa oyunu tekrar baslatabiliriz.

			font2.draw(batch,"Game Over! Tap To Play Again!",100,Gdx.graphics.getHeight()/2);

			if (Gdx.input.justTouched()){//kullanıcı dokundugunda ne olacak ?

				gameState=1;//basladı ve 1 oldu.
				birdY=Gdx.graphics.getHeight()/3;//basa aldık birdY yi.
				for (int i=0;i<numberOfEnemies;i++){

					enemyOffset1[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);//bize bir yuzde verecek.
					enemyOffset2[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);//bize bir yuzde verecek.
					enemyOffset3[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);//bize bir yuzde verecek.



					enemyX[i]=Gdx.graphics.getWidth() - bee1.getWidth()/2 + i*distance;//arılar arası mesafe oluyor

					enemyCircles1[i]=new Circle();
					enemyCircles2[i]=new Circle();
					enemyCircles3[i]=new Circle();

				}

				velocity=0;//cok yukarlara cıktıysa bastan baslamasını istedik ve sıfırladık.
				scoreEnemy=0;
				score=0;

			}

		}

		//ucma kacma hepsi render altında olacak.

		batch.draw(bird,birdX,birdY,Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);


		font.draw(batch,String.valueOf(score),100,200);//fontu yazdırmıs olduk batch kullanarak.



		batch.end();

		birdCircle.set(birdX+Gdx.graphics.getWidth()/30,birdY+Gdx.graphics.getHeight()/20 ,Gdx.graphics.getWidth()/30);//circle cizdik.

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLACK);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);


		for (int i=0 ; i<numberOfEnemies ;  i++){

			//shapeRenderer.circle(enemyX[i]+  +Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset1[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
			//shapeRenderer.circle(enemyX[i]+  +Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset2[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
			//shapeRenderer.circle(enemyX[i]+  +Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2+enemyOffset3[i]+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);

			if (Intersector.overlaps(birdCircle,enemyCircles1[i]) || Intersector.overlaps(birdCircle,enemyCircles2[i]) || Intersector.overlaps(birdCircle,enemyCircles3[i])){
				//carpısmalarda olacakları yazıyoruz.

				//simdi oyunu bitiriyoruz.
				gameState=2;//gamestatin 2 oldugu zamanıda yukarda yazıyoruz.


			}

		}

	//shapeRenderer.end();//shaperendered ı bitirdik.


	}
	
	@Override
	public void dispose () {


	}
}
