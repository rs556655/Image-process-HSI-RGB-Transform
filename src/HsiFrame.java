import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class HsiFrame extends JFrame {

	private static final long serialVersionUID = 4390493953307669741L;
	JPanel cotrolPanel = new JPanel();
	ImagePanel imagePanel = new ImagePanel();
	JButton btnShow = new JButton("顯示"), 
			btnH = new JButton("調整 Hue"), 
			btnS = new JButton("調整 Saturation"),
			btnI = new JButton("調整 Intensity"), 
			btnHSI = new JButton("調整 HSI"), 
			btnExtra = new JButton("萃取");
	
	JPanel showPanel = new JPanel();
	JPanel hsiPanel = new JPanel();
	JPanel extraPanel = new JPanel();
				
	JSlider hControl = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
	JSlider sControl = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);
	JSlider iControl = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);
	JSlider extraControl = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
	
	JLabel 
		label_h = new JLabel("Hue (度)"),
		label_s = new JLabel("Saturation (%)"),
		label_i = new JLabel("Intensity (%)"),
		label_extra = new JLabel("萃取　"),
		label_h_pre = new JLabel("0"),
		label_s_pre = new JLabel("0"),
		label_i_pre = new JLabel("0"),
		label_extra_pre = new JLabel("0");
	
	final int[][][] data;
	int height, width;
	BufferedImage img = null;
	
	ActionListener buttonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == btnH) 
				imagePanel.showImage(data[0].length, data.length, exeH(data, hControl.getValue()));
			else if (e.getSource() == btnS) 
				imagePanel.showImage(data[0].length, data.length, exeS(data, sControl.getValue()));
			else if (e.getSource() == btnI) 
				imagePanel.showImage(data[0].length, data.length, exeI(data, iControl.getValue()));
			else if (e.getSource() == btnHSI) 
				imagePanel.showImage(data[0].length, data.length, exeI(exeS(exeH(data, hControl.getValue()), sControl.getValue()),iControl.getValue()));
			else if (e.getSource() == btnExtra) 
				imagePanel.showImage(data[0].length, data.length, exeExtra(data, extraControl.getValue()));
			else imagePanel.showImage(width, height, data);
			
			imagePanel.setBounds(
					((HsiFrame.this.getWidth() - img.getWidth()) / 2),
					((HsiFrame.this.getHeight() - 250 - img.getHeight()) / 2),
					600,600);
		}
	};
	
	ChangeListener controlValueChangeListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			JLabel target;
			if (e.getSource() == hControl) target = label_h_pre;
			else if (e.getSource() == sControl) target = label_s_pre;
			else if (e.getSource() == iControl) target = label_i_pre;
			else if (e.getSource() == extraControl) target = label_extra_pre;
			else return;
			
			target.setText(String.valueOf(((JSlider)e.getSource()).getValue()));
		}
	};
	
	protected HsiFrame(){
		setTitle("影像處理 HSI by 410275024 陳品豪");
		
		try {
		    img = ImageIO.read(new File("file/Munich.png"));
		} catch (IOException e) {
			System.out.println("IO exception");
		}
		
		height = img.getHeight();
		width = img.getWidth();
		data = new int[height][width][3]; 
		
		this.setSize(width + 15, height + 77);
		
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				int rgb = img.getRGB(x, y);
				data[y][x][0] = Utils.getR(rgb);
				data[y][x][1] = Utils.getG(rgb);
				data[y][x][2] = Utils.getB(rgb);
			}
		
		// 事件監聽
		btnShow.addActionListener(buttonActionListener);
		btnH.addActionListener(buttonActionListener);
		btnS.addActionListener(buttonActionListener);
		btnI.addActionListener(buttonActionListener);
		btnHSI.addActionListener(buttonActionListener);
		btnExtra.addActionListener(buttonActionListener);
		
		hControl.addChangeListener(controlValueChangeListener);
		sControl.addChangeListener(controlValueChangeListener);
		iControl.addChangeListener(controlValueChangeListener);
		extraControl.addChangeListener(controlValueChangeListener);

		// 加入元件
		showPanel.add(btnShow);
		
		hsiPanel.setLayout(new GridLayout(4, 6));
		hsiPanel.add(new JPanel());
		hsiPanel.add(label_h);
		hsiPanel.add(hControl);
		hsiPanel.add(label_h_pre);
		hsiPanel.add(btnH);
		hsiPanel.add(new JPanel());
		hsiPanel.add(new JPanel());
		hsiPanel.add(label_s);
		hsiPanel.add(sControl);
		hsiPanel.add(label_s_pre);
		hsiPanel.add(btnS);
		hsiPanel.add(new JPanel());
		hsiPanel.add(new JPanel());
		hsiPanel.add(label_i);
		hsiPanel.add(iControl);
		hsiPanel.add(label_i_pre);
		hsiPanel.add(btnI);
		hsiPanel.add(new JPanel());
		hsiPanel.add(new JPanel());
		hsiPanel.add(new JLabel());
		hsiPanel.add(new JLabel());
		hsiPanel.add(new JLabel());
		hsiPanel.add(btnHSI);
		hsiPanel.add(new JPanel());
		
		extraPanel.setLayout(new GridLayout(1,6));
		extraPanel.add(new JPanel());
		extraPanel.add(label_extra);
		extraPanel.add(extraControl);
		extraPanel.add(label_extra_pre);
		extraPanel.add(btnExtra);
		extraPanel.add(new JPanel());
		
		// 控制面板
		cotrolPanel.setLayout(new BoxLayout(cotrolPanel, BoxLayout.Y_AXIS));
		cotrolPanel.add(showPanel);
		cotrolPanel.add(new JPanel());
		cotrolPanel.add(hsiPanel);
		cotrolPanel.add(new JPanel());
		cotrolPanel.add(extraPanel);
		cotrolPanel.add(new JPanel());
		
		// 多加一塊板來讓 imagePanel 可以顯示在正中間
		JPanel temp = new JPanel();
		temp.setLayout(null);
		temp.add(imagePanel);
		
		// 主畫面
		setLayout(new BorderLayout());	 
	    add(cotrolPanel, BorderLayout.PAGE_START);
	    add(temp, BorderLayout.CENTER);
	}
	
	private int [][][] exeH(int [][][] data, int offset) {
		if (offset == 0.0) return data;
		int [][][] ndata = new int [data.length][data[0].length][3];
		
		for (int y = 0; y < data.length; y++) {
			for (int x = 0; x < data[0].length; x++) {
				int [] color = data[y][x];
				int hue = Utils.getHueFromRGB(color[0], color[1], color[2]);
				double sat = Utils.getSatFromRGB(color[0], color[1], color[2]);
				double inte = Utils.getIntFromRGB(color[0], color[1], color[2]);
				
				hue += offset;
				
				if (hue > 360) hue -= 360;
				else if (hue < 0) hue += 360;
				
				ndata[y][x] = Utils.getRGBFromHSI(hue, sat, inte);
			}
		}
		return ndata;
	}
	
	private int [][][] exeS(int [][][] data, double offset) {
		if (offset == 0.0) return data;
		offset /= 100.0;
		
		int [][][] ndata = new int [data.length][data[0].length][3];
		
		for (int y = 0; y < data.length; y++) {
			for (int x = 0; x < data[0].length; x++) {
				int [] color = data[y][x];
				int hue = Utils.getHueFromRGB(color[0], color[1], color[2]);
				double sat = Utils.getSatFromRGB(color[0], color[1], color[2]);
				double inte = Utils.getIntFromRGB(color[0], color[1], color[2]);
				
				sat += (offset>0?(1.0-sat):(sat)) * offset;
				
				ndata[y][x] = Utils.getRGBFromHSI(hue, Utils.checkHSIConvertBound(sat), inte);
			}
		}
		return ndata;
	}
	
	private int [][][] exeI(int [][][] data, double offset) {
		if (offset == 0.0) return data;
		offset /= 100.0;
		
		int [][][] ndata = new int [data.length][data[0].length][3];
		
		for (int y = 0; y < data.length; y++) {
			for (int x = 0; x < data[0].length; x++) {
				int [] color = data[y][x];
				int hue = Utils.getHueFromRGB(color[0], color[1], color[2]);
				double sat = Utils.getSatFromRGB(color[0], color[1], color[2]);
				double inte = Utils.getIntFromRGB(color[0], color[1], color[2]);

				inte += ((offset>0.0?(1.0-inte):(inte)) * offset);
				
				ndata[y][x] = Utils.getRGBFromHSI(hue, sat, Utils.checkHSIConvertBound(inte));
			}
		}
		return ndata;
	}
	
	private int [][][] exeExtra(int [][][] data, int value) {
		
		int [][][] ndata = new int [data.length][data[0].length][3];
		
		for (int y = 0; y < data.length; y++) {
			for (int x = 0; x < data[0].length; x++) {
				int [] color = data[y][x];
				int newColor = Utils.getGrayScale(color[0], color[1], color[2])>=value?255:0;
				ndata[y][x][0] = ndata[y][x][1] = ndata[y][x][2] = newColor;
			}
		}
		return ndata;
	}
}
