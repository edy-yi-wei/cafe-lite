package com.besoft.cafelite.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.besoft.cafelite.dto.SoldMenu;
import com.besoft.cafelite.model.CashierSession;
import com.besoft.cafelite.model.Invoice;
import com.besoft.cafelite.model.InvoiceDetail;
import com.besoft.cafelite.report.CashierSessionReport;
import com.besoft.cafelite.report.InvoiceReport;
import com.besoft.cafelite.report.SoldMenuReport;
import com.besoft.cafelite.repository.ReportRepository;
import com.besoft.cafelite.utilities.SoldMenuSort;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class ReportService {
	@Autowired
	private ReportRepository repo;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String destinationEmail = "";
	
	public void downloadReports(Date startDate, Date endDate) throws Exception {
		logger.info("ReportService - downloadReports [start date: "+startDate+", end date: "+endDate+"]");
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
        Locale locale = new Locale("in", "ID");
        SimpleDateFormat fmt = new SimpleDateFormat("dd MMMM yyyy");
        String jasperFilePath = System.getProperty("user.dir") + "/tmps/";
        List<String> files = new ArrayList<>();
        
		try {
			/* --LAPORAN PENJUALAN-- */
			List<Invoice> invoices = selectInvoice(startDate, endDate);
            if(!invoices.isEmpty()){
				File file = new File(jasperFilePath+"reportInvoice.jasper");
				Map<String, Object> params = new HashMap<>();
				params.put("startDate", fmt.format(startDate));
				params.put("endDate", fmt.format(endDate));
				jasperReport = (JasperReport) JRLoader.loadObject(file);
				
				jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(invoices));
            
				String outputPath = jasperFilePath+"reportInvoice.pdf";
				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputPath);
				exporter.exportReport();
				files.add(outputPath);
            }
            
            /* --LAPORAN OMZET KASIR-- */
            List<CashierSession> sessions = selectCashierSession(startDate, endDate);
            if(!sessions.isEmpty()){
            	List<CashierSessionReport> sessionReport = new ArrayList<>();
            	for(CashierSession s: sessions) {
    				CashierSessionReport rpt = new CashierSessionReport();
    				rpt.setSessionId(s.getSessionId());
    				rpt.setCashierName(s.getCashier().getUserName());
    				rpt.setLoginTime(s.getLoginTime());
    				rpt.setLogoutTime(s.getLogoutTime());
    				rpt.setCashAmount(s.getCashAmount());
    				sessionReport.add(rpt);
    			}
				File file = new File(jasperFilePath+"reportCashierSession.jasper");
				Map<String, Object> params = new HashMap<>();
				params.put("startDate", fmt.format(startDate));
				params.put("endDate", fmt.format(endDate));
				jasperReport = (JasperReport) JRLoader.loadObject(file);
				
				jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(sessionReport));
            
				String outputPath = jasperFilePath+"reportCashierSession.pdf";
				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputPath);
				exporter.exportReport();
				files.add(outputPath);
            }
            
            /* --LAPORAN SOLD MENU-- */
            List<SoldMenu> menus = selectSoldMenu(startDate, endDate);
            if(!menus.isEmpty()){
            	List<SoldMenuReport> soldMenus = new ArrayList<>();
            	for(SoldMenu s: menus) {
    				SoldMenuReport rpt = new SoldMenuReport();
    				rpt.setMenuId(s.getMenu().getMenuId());
    				rpt.setMenuName(s.getMenu().getMenuName());
    				rpt.setQuantity(s.getQuantity());
    				soldMenus.add(rpt);
    			}
				File file = new File(jasperFilePath+"reportSoldMenu.jasper");
				Map<String, Object> params = new HashMap<>();
				params.put("startDate", fmt.format(startDate));
				params.put("endDate", fmt.format(endDate));
				jasperReport = (JasperReport) JRLoader.loadObject(file);
				
				jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(soldMenus));
            
				String outputPath = jasperFilePath+"reportSoldMenu.pdf";
				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outputPath);
				exporter.exportReport();
				files.add(outputPath);
            }
            
            /* --SEND REPORT VIA EMAIL-- */
            String to = destinationEmail; 
            final String username = "";
            final String password = "";
            String host="smtp.gmail.com";
            String port = "465";
            java.util.Properties props = null;
            props = System.getProperties();
            props.put("mail.smtp.user", username);
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.debug", "true");
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.socketFactory.port", port);
			props.put("mail.smtp.starttls.enable",true);
			props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", false);
			
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			    	return new PasswordAuthentication(username, password);
			      }
			});
			session.setDebug(false);
			
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(username));
			msg.setSubject("Laporan Cindy's Fried Chicken");
			msg.setText("", "ISO-8859-1");
			msg.setSentDate(new Date());
			msg.setHeader("content-Type", "text/html;charset=\"ISO-8859-1\"");
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText("Silahkan download attachment");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			for(String file: files) {
				messageBodyPart = new MimeBodyPart();
				String filename = file;
				DataSource source = new FileDataSource(filename);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(filename);
				multipart.addBodyPart(messageBodyPart);
			}
			msg.setContent(multipart );
			//                  msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			msg.saveChanges();
			
			Transport transport = session.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		} catch(Exception ex) {
			logger.info("ERROR ReportService - downloadReports "+ex.getMessage());
			throw ex;
		}
	}
	
	private List<Invoice> selectInvoice(Date startDate, Date endDate){
		logger.info("ReportService - selectInvoice[start date: "+startDate+ ", end date: "+endDate+"]");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add(Calendar.DATE, 1);
		cal.add(Calendar.SECOND, -1);
		endDate = cal.getTime();		
		return repo.findInvoiceByPeriod(startDate, endDate);
	}
	
	public List<CashierSession> selectCashierSession(Date startDate, Date endDate){
		logger.info("ReportService - selectCashierSession [start date: "+startDate+ ", end date: "+endDate+"]");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add(Calendar.DATE, 1);
		cal.add(Calendar.SECOND, -1);
		endDate = cal.getTime();
		return repo.findCashierSessionByPeriod(startDate, endDate);
	}
	
	public List<SoldMenu> selectSoldMenu(Date startDate, Date endDate){
		logger.info("ReportService - selectSoldMenu [start date: "+startDate+ ", end date: "+endDate+"]");
		
		List<SoldMenu> result = new ArrayList<>();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add(Calendar.DATE, 1);
		cal.add(Calendar.SECOND, -1);
		endDate = cal.getTime();
		List<Invoice> invoices = selectInvoice(startDate, endDate);
		for(Invoice i: invoices) {
			mergeToList(i, result);
		}
		Collections.sort(result, new SoldMenuSort());
		return result;
	}
	
	private void mergeToList(Invoice i, List<SoldMenu> list) {
		boolean found = false;
		for(InvoiceDetail d: i.getDetails()) {
			for(SoldMenu s: list) {
				if(d.getMenu().getMenuId().longValue()==s.getMenu().getMenuId().longValue()) {
					Double nilai = s.getQuantity() + d.getQuantity();
					s.setQuantity(nilai);
					found=true;
					break;
				}
			}
			if(!found) {
				SoldMenu m = new SoldMenu();
				m.setMenu(d.getMenu());
				m.setQuantity(d.getQuantity());
				list.add(m);
			}
		}
	}
}
