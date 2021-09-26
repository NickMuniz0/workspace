package br.com.wjg.view;

import java.awt.Component;
import java.awt.Label;
import java.awt.Toolkit;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Mensagens {
	private String labelFrame = "Atenção!";
	private JFrame frame;
    private JPanel panel;
    private GroupLayout layout;  
    private Label mensagem ;
    
    public Mensagens(String mensagem){
    	setMensagem(mensagem);
    	Build();
    }

    public JFrame getFrame() {
		return frame;
	}
    public GroupLayout getLayout() {
		return layout;
	}
    public JPanel getPanel() {
		return panel;
	}
    
    public void setFrame(String labelFrame) {
    	int largura = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int altura = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.frame = new JFrame(labelFrame);
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		this.frame.setSize(200,200);	
		this.frame.setBounds((largura/3)+50, (altura/4)+100, 400, 200);

	}
    
    public void setLayout(JPanel panel) {
		this.layout  = new GroupLayout(panel);
		this.layout.setAutoCreateGaps(true);
		this.layout.setAutoCreateContainerGaps(true);
	}
    
    public void setPanel() {
		this.panel = new JPanel();
	}
    public void setAddPanel(Component compomente) {
		this.panel.add(compomente);
	}
    
    public String getLabelFrame() {
		return labelFrame;
	}
    
    public Label getMensagem() {
		return mensagem;
	}    
    public void setMensagem(String mensagem) {
		this.mensagem = new Label(mensagem);
	}
    
    public void Build() {
    	setFrame(getLabelFrame());	
    	setPanel();
    	setLayout(getPanel());

    	
    	setAddPanel(getMensagem());
    	getLayout().setHorizontalGroup(getLayout().createSequentialGroup()
    			
    		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.CENTER)    		
    		                    .addComponent(getMensagem())
    		                    )
    	);
    	
    	
    	getLayout().setVerticalGroup(getLayout().createSequentialGroup()
    		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.CENTER)
    		            .addComponent(getMensagem())
    		    		));
    	
    	getPanel().setLayout(getLayout());   
	    getFrame().getContentPane().add(getPanel()); 
	    getFrame().setVisible(true);
    	
    	
    }
	
	
}
