package core;

import java.awt.Color;

public class LargeSeed {
	/**
	 * This array contains the pixel data for the seed
	 * Two pixels are contained in one byte like so:
	 * [0 1 1 0][1 0 1 1]
	 *  Pixel 1  Pixel 2
	 */
	private byte[] bytes;
	
	/**
	 * Constructs a large seed
	 * @param size The amount of pixels in the image to generate (100x100 would be 10,000)
	 */
	public LargeSeed(int size){
		bytes = new byte[size/2];
	}
	
	/**
	 * Prints the value of this seed to the console
	 */
	public void print(){
		for (int i = bytes.length - 1; i >= 0; --i){
			byte a = bytes[i];
			if (a==0){
				continue;
			}
			for (int j = 0; j < 8; ++j){
				System.out.print((a & 1) + " ");
				a = (byte) (a >> 1);
			}
			System.out.println();
		}
	}
	
	/**
	 * Increments the seed
	 */
	public void increment(){
		boolean carry = true;
		int index = 0;
		while (carry && index <= bytes.length){
			bytes[index] = (byte) (bytes[index] + 1);
			if (bytes[index] < 0){
				bytes[index] = 0;
				index++;
			} else {
				carry = false;
			}
		}
	}
	
	public int[] getImageData(){
		int[] data = new int[this.bytes.length * 2];
		
		for (int i = 0; i < this.bytes.length; ++i){
			//First half
			byte a = this.bytes[i];
			int value = (a & 15) * 16; //0-240
			data[i*2] = new Color(value, value, value).getRGB();
			
			a = (byte) (a >> 4);
			
			value = (a & 15) * 16; //0-240
			data[(i*2)+1] = new Color(value, value, value).getRGB();
		}
		
		return data;
	}
}
