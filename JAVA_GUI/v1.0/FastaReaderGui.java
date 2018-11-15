import java.io.*;
import java.awt.*;
import java.awt.Desktop;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

public class FastaReaderGui extends JPanel implements ActionListener {
	private final String url = "https://github.com/KennethJHan/FastaReader/tree/master/JAVA_GUI";
	static private final String newline = "\n";
	JButton openButton, saveButton;
	JTextArea log;
	JFileChooser fc;

	public FastaReaderGui() {
		super(new BorderLayout());

		//Create the log first, because the action listeners need to refer to it.
		log = new JTextArea(5,20);
		log.setMargin(new Insets(5,5,5,5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		//Create a file chooser
		fc = new JFileChooser();
		openButton = new JButton("Open a Fasta File...", createImageIcon("images/folder.png"));
		//openButton = new JButton("Open a File...");
		openButton.addActionListener(this);

		JPanel buttonPanel = new JPanel(); //use FlowLayout
		buttonPanel.add(openButton);


		JPanel statusPanel = new JPanel();
		JLabel statusLabel = new JLabel("Fasta Reader GUI v1.0");
		JButton statusButton = new JButton("Visit Developer's GitHub page");
		statusPanel.add(statusLabel);
		statusPanel.add(statusButton);
		statusButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
				} catch(IOException ex) {
					System.out.println(ex);
				}
			}
		});

		add(buttonPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);
		add(statusPanel, BorderLayout.PAGE_END);
	}

	public void actionPerformed(ActionEvent e) {
		//Handle open button action
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(FastaReaderGui.this);

			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				//This is where a real application would open the file
				//log.append("Opening: "+file.getName()+"."+newline+newline);
				long start = System.currentTimeMillis();
				long[] cntArray = readFasta(file);
				long finish = System.currentTimeMillis();
				double timeElapsed = ((double)finish - (double)start)/1000.0;
				long cntA = cntArray[0];
				long cntC = cntArray[1];
				long cntG = cntArray[2];
				long cntT = cntArray[3];
				long cntN = cntArray[4];
				long cntTotal = cntArray[5];
				log.append("## Read "+file.getName()+" in "+timeElapsed+" seconds##"+newline);
				log.append("TotalBase: "+cntTotal+newline);
				log.append("A: "+cntA+newline);
				log.append("C: "+cntC+newline);
				log.append("G: "+cntG+newline);
				log.append("T: "+cntT+newline);
				log.append("N: "+cntN+newline+newline);
			} else {
				log.append("Open command cancelled by user."+newline);
			}
			log.setCaretPosition(log.getDocument().getLength());
		}
	}

	public long[] readFasta(File file) {
		long cntA = 0;
		long cntC = 0;
		long cntG = 0;
		long cntT = 0;
		long cntN = 0;
		long cntTotal = 0;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String s = null;
			while((s=br.readLine())!=null) {
				if(!(s.startsWith(">"))){
					for(char ch: s.toCharArray()) {
						if (ch == 'A') {
							cntA++;
						} else if (ch == 'C') {
							cntC++;
						} else if (ch == 'G') {
							cntG++;
						} else if (ch == 'T') {
							cntT++;
						} else if (ch == 'N') {
							cntN++;
						}
					}
				}
			}
		} catch(IOException ex) {
			System.out.println("Error: "+ex);
		}
		cntTotal = cntA + cntC + cntG + cntT + cntN;
		long[] cntArray = { cntA, cntC, cntG, cntT, cntN, cntTotal };
		return cntArray;
	}

	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = FastaReaderGui.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: "+path);
			return null;
		}
	}

	private static void createAndShowGUI() {
		//Create and set up the window
		JFrame frame = new JFrame("FastaReaderGui");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add content to the window
		frame.add(new FastaReaderGui());

		//Display the window
		//frame.pack();
		frame.setSize(500,400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true); 
	}

	public static void main(String[] args) {
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}
}
