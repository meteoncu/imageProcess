package perceive_borders;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class borders{
	
	public static BufferedImage resim;
	
	public static void main(String[] args) throws IOException{
		try{
			resim=ImageIO.read(new File("C:\\workspace\\rgb\\src\\rgb\\resim.jpg"));
		}
		catch(IOException e){}
		
		BufferedImage sinirlar1=new BufferedImage(resim.getWidth(),resim.getHeight(),BufferedImage.TYPE_INT_RGB);
		BufferedImage sinirlar2=new BufferedImage(resim.getWidth(),resim.getHeight(),BufferedImage.TYPE_INT_RGB);
		BufferedImage sinirlar3=new BufferedImage(resim.getWidth(),resim.getHeight(),BufferedImage.TYPE_INT_RGB);
		
		int tolerans=5;
		int min_uzunluk=50;
		int baglanti_kopma_uzakligi=50;
		int paint_color = (255 << 16) | (255 << 8) | 255;
		double hassasiyet=0.2;
		int[][] anagruplar=new int[100000][];
		int ana_grup_no=0;
			
		for(int y=0;y<resim.getHeight();y++){
			int son_grup_no=0;
			int[][] gruplar=new int[resim.getWidth()][];
			
			for(int x=0;x<resim.getWidth();x++){
				if(x>0){
					Color rgb=new Color(resim.getRGB(x,y));
					double red=rgb.getRed();
					double green=rgb.getGreen();
					double blue=rgb.getBlue();
					if(red==0){red++;}
					if(green==0){green++;}
					if(blue==0){blue++;}
					double toplamRgb=red+green+blue;
					double redOran=red/toplamRgb;
					double greenOran=green/toplamRgb;
					double blueOran=blue/toplamRgb;
					
					int grup_bulundu=0;
					for(int i=0;i<son_grup_no;i++){
						double redFark=Math.abs(redOran-(double)gruplar[i][2]/10);
						double greenFark=Math.abs(greenOran-(double)gruplar[i][3]/10);
						double blueFark=Math.abs(blueOran-(double)gruplar[i][4]/10);
						if(redFark<=hassasiyet && greenFark<=hassasiyet && blueFark<=hassasiyet){
							if(x-gruplar[i][0]<=baglanti_kopma_uzakligi){
								gruplar[i][1]=x;
								grup_bulundu=1;
							}
						}
					}
					
					if(grup_bulundu==0){
						search:{
							for(int i=1;i<=tolerans;i++){
								if(x-i>=0){
									Color buRgb=new Color(resim.getRGB((x-i),y));
									double buRed=buRgb.getRed();
									double buGreen=buRgb.getGreen();
									double buBlue=buRgb.getBlue();
									if(buRed==0){buRed++;}
									if(buGreen==0){buGreen++;}
									if(buBlue==0){buBlue++;}
									double buToplamRgb=buRed+buGreen+buBlue;
									double buRedOran=buRed/buToplamRgb;
									double buGreenOran=buGreen/buToplamRgb;
									double buBlueOran=buBlue/buToplamRgb;
									
									double redFark=Math.abs(redOran-buRedOran);
									double greenFark=Math.abs(greenOran-buGreenOran);
									double blueFark=Math.abs(blueOran-buBlueOran);
									
									if(redFark<=hassasiyet && greenFark<=hassasiyet && blueFark<=hassasiyet){
										gruplar[son_grup_no]=new int[5];
										gruplar[son_grup_no][0]=x-i;
										gruplar[son_grup_no][1]=x;
										gruplar[son_grup_no][2]=(int) (redOran*10);
										gruplar[son_grup_no][3]=(int) (greenOran*10);
										gruplar[son_grup_no][4]=(int) (blueOran*10);
										son_grup_no++;
										break search;
									}
								}else{
									break search;
								}//geride bakılacak piksel var mı
							}//tolerans kadar geriye bakıyoruz
						}//search
					}//grup bulunamadıysa sonu
					
				}//if x>0
			}//xler
			
			for(int i=0;i<son_grup_no;i++){
				if(gruplar[i][2]-gruplar[i][1]>=min_uzunluk){
					anagruplar[ana_grup_no]=new int[7];
					anagruplar[ana_grup_no][0]=gruplar[i][0];//baslangıç x
					anagruplar[ana_grup_no][1]=y;//başlangıç y
					anagruplar[ana_grup_no][2]=gruplar[i][1];//bitiş x
					anagruplar[ana_grup_no][3]=y;//bitiş y
					anagruplar[ana_grup_no][4]=gruplar[i][2];
					anagruplar[ana_grup_no][5]=gruplar[i][3];
					anagruplar[ana_grup_no][6]=gruplar[i][4];
					ana_grup_no++;
				}//grubun uzunluğu min_uzunluktan büyük eşitse
			}//mevcut grupları döndürüyoruz
			
		}//yler
		
	
		for(int i=0;i<ana_grup_no;i++){
			sinirlar1.setRGB(anagruplar[i][0],anagruplar[i][1],paint_color);
			
			sinirlar2.setRGB(anagruplar[i][2],anagruplar[i][3],paint_color);
			
			sinirlar3.setRGB(anagruplar[i][0],anagruplar[i][1],paint_color);
			sinirlar3.setRGB(anagruplar[i][2],anagruplar[i][3],paint_color);
		}
		
		File f1 = new File("C:\\workspace\\rgb\\src\\rgb\\foto1.jpg");
		ImageIO.write(sinirlar1, "JPEG", f1);
		
		File f2 = new File("C:\\workspace\\rgb\\src\\rgb\\foto2.jpg");
		ImageIO.write(sinirlar2, "JPEG", f2);
		
		File f3 = new File("C:\\workspace\\rgb\\src\\rgb\\foto3.jpg");
		ImageIO.write(sinirlar3, "JPEG", f3);
				
	}
}