package com.besoft.cafelite.utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class Printer implements Printable{
	private final double INCH = 72;
	private PrinterJob printJob = PrinterJob.getPrinterJob();
	private List<PrintElement> contents;
	
	public Printer() {
//	    printJob.setPrintable(this);
	    PageFormat pf = printJob.defaultPage();
		Paper paper = new Paper();
		
		paper.setImageableArea(10, 10, paper.getWidth()-10, paper.getHeight()-10);
		pf.setPaper(paper);
		printJob.setPrintable(this, pf);
	}
 
	public void print(List<PrintElement> contents) {
		try {
			this.contents = contents;
			printJob.print();
		} catch(Exception printException) {
			printException.printStackTrace();
		}
	}
	
	/**
	 * Method: print
	 * <p>
	 * 
	 * This class is responsible for rendering a page using the provided
	 * parameters. The result will be a grid where each cell will be half an
	 * inch by half an inch.
	 * 
	 * @param g
   	 * a value of type Graphics
   	 * @param pageFormat
   	 * a value of type PageFormat
   	 * @param page
   	 * a value of type int
   	 * @return a value of type int
   	 */
	public int print(Graphics g, PageFormat pageFormat, int page) {
 
		int i;
		Graphics2D g2d;
		Line2D.Double line = new Line2D.Double();
		
		//--- Validate the page number, we only print the first page
		if (page == 0) {  //--- Create a graphic2D object a set the default parameters
			g2d = (Graphics2D) g;
			g2d.setColor(Color.black);
			Font font = new Font("Arial", Font.PLAIN, 10);
			g2d.setFont(font);
 
			//--- Translate the origin to be (0,0)
//			System.out.println(pageFormat.getImageableX());
//			System.out.println(pageFormat.getImageableY());
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
//			g2d.translate(0, 0);
			
			if(contents!=null) {
				for(PrintElement element: contents) {
//					System.out.println(element.getContent());
					switch(element.getContent()) {
					case "-":
						line.setLine(element.getX(), element.getY(), pageFormat.getWidth(), element.getY());
						g2d.draw(line);
						break;
					case "=":
						line.setLine(element.getX(), element.getY(), pageFormat.getWidth(), element.getY());
						g2d.draw(line);
						line.setLine(element.getX(), element.getY()+2, pageFormat.getWidth(), element.getY()+2);
						g2d.draw(line);
						break;
					default:
						g2d.drawString(element.getContent(), element.getX(), element.getY());
					}
				}
			}
			
			//--- Print the vertical lines
//			for (i = 0; i < pageFormat.getWidth(); i += INCH / 2) {
//				line.setLine(i, 0, i, pageFormat.getHeight());
//				g2d.draw(line);
//			}
 
			//--- Print the horizontal lines
//			for (i = 0; i < pageFormat.getHeight(); i += INCH / 2) {
//				line.setLine(0, i, pageFormat.getWidth(), i);
//				g2d.draw(line);
//			}
 
			return (PAGE_EXISTS);
		} else {
			return (NO_SUCH_PAGE);
		}
	}
}
