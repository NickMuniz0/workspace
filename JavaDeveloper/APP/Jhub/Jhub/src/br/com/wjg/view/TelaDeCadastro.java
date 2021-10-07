package br.com.wjg.view;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.simple.JSONObject;

import br.com.wjg.config.Git;
import br.com.wjg.models.Autor;
import br.com.wjg.models.Historico;
import br.com.wjg.models.Objeto;
import br.com.wjg.models.Sinistro;
import br.com.wjg.modulos.FileControl;

public class TelaDeCadastro {
	
	private String labelFrame = "Relório";
    private String labelRelatorio = "Informações do Relatório";
    private String labelAutor = "Autor";
    private String labelArquivoModificado ="Arquivo Modificado";
    private String labelDescricao= "Descreva as Modificações";
    private String labelLinhasAlteradas = "Linhas Alteradas";
    private String labelObservacoesAdicionais = "Observações Adicionais";
    private String labelGravidade = "Gravidade";
    private String labelGit = "Configurações para o Git";
    private String labelPathRepositorio = "Path do Repositório";
    private String labelBranch = "Branch";
    private String labelComentarios = "Comentários";
    private String labelSalvarRelatorio = "Salvar Relatório";
    private String labelEnviarParaOGit = "Enviar para o git";
    private String labelSalvarConfiguracao = "Salvar Configurações";
    private String labelResult = "Resultado";
    private String labelListBranchsName = "Listar Branchs";
    private String labelArquivoGit = "O objeto";
    private String labelDefinaObjeto = "Defina o objeto";


    private JFrame frame ;
    private JPanel panel;
    private GroupLayout layout;    
    private JLabel labelInfoFile ;
    private JLabel autorL;
    private JTextField jtautor;
    private JLabel arquivo;
    private JTextField jtarquivo;
    private JLabel descricao ;
    private JTextField jtdescricao;
    private JLabel localizacao;
    private JTextField jtlocalizacao;
    private JLabel observacoes;
    private JTextField jtobservacoes;
    private JLabel sinistro;
    private JComboBox<Sinistro> jtsinistro;    
    private JLabel labelInfoGit;
    private JLabel arquivoGit;
    private JTextField jtarquivoGit;  
    private JLabel repositorioGit;
    private JTextField jtrepositorioGit;
    private JLabel branchGit;
    private JTextField jtbranchGit;    
    private JLabel comentarioGit;
    private JTextField jtcomentarioGit;
    private JButton salvar;
    private JButton git; 
    private JButton salvarConfig;
    private JButton listbranchs;
    private Autor autor;
    private Historico history;
    private Git gitConf;    
    private JSONObject historico;
    private JSONObject datas;
    private JSONObject autores;
    private FileControl wf; 
    private JLabel result;
    private JTextArea jtresult;
    
    private JLabel definaObjeto;
    private JComboBox<Objeto> jtdefinaobjeto;    
    private JScrollPane scroll;



    
    public TelaDeCadastro(){
    	setAutores();
    	setHistorico();
    	setDatas();        	
	    setAutor();
	    setHistory();
	    setGitConf();	    
    	configuraComponentes();
    	FormatLayout();
    	GetValue();
    	botaoCadastrar();
    	botaoGit();
    	SaveConfig();
        setWf();
        botaoViewBranchs();

    	
    }
    
    public JSONObject getAutores() {
		return autores;
	}
    
    public void setAutores() {
		this.autores = new JSONObject();
	}

    public JSONObject getHistorico() {
		return historico;
	}
    
    public void setHistorico() {
		this.historico = new JSONObject();
	}
    
    public JSONObject getDatas() {
		return datas;
	}
    
    public void setDatas() {
		this.datas = new JSONObject();
	}
    
    public Autor getAutor() {
		return autor;
	}
    
    public void setAutor() {
		this.autor = new Autor();
	}
   
    public Historico getHistory() {
		return history;
	}
    
    public void setHistory() {
		this.history = new Historico();
	}
    
    public Git getGitConf() {
		return gitConf;
	}
    
    public void setGitConf() {
		this.gitConf = new Git();
	}
    
    public String getLabelFrame() {
		return labelFrame;
	}
	
    public String getLabelRelatorio() {
		return labelRelatorio;
	}

	public String getLabelAutor() {
		return labelAutor;
	}
	
	public String getLabelArquivoModificado() {
		return labelArquivoModificado;
	}
	
	public String getLabelDescricao() {
		return labelDescricao;
	}
	
	public String getLabelLinhasAlteradas() {
		return labelLinhasAlteradas;
	}
	
	public String getLabelObservacoesAdicionais() {
		return labelObservacoesAdicionais;
	}
	
	public String getLabelGravidade() {
		return labelGravidade;
	}
	
	public String getLabelGit() {
		return labelGit;
	}
	
	public String getLabelArquivoGit() {
		return labelArquivoGit;
	}
	
	public String getLabelPathRepositorio() {
		return labelPathRepositorio;
	}
	
	public String getLabelBranch() {
		return labelBranch;
	}
	
	public String getLabelComentarios() {
		return labelComentarios;
	}
	
	public String getLabelSalvarRelatorio() {
		return labelSalvarRelatorio;
	}
	
	public String getLabelEnviarParaOGit() {
		return labelEnviarParaOGit;
	}
	
	public String getLabelSalvarConfiguracao() {
		return labelSalvarConfiguracao;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public void setFrame(String labelFrame) {
		int largura = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int altura = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.frame = new JFrame(labelFrame);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.frame.setSize(800,500);		
		this.frame.setBounds((largura/5), (altura/15), 800, 650);
	}
		
	public JPanel getPanel() {
		return panel;
	}
	
	public void setPanel() {
		this.panel = new JPanel();
	}
	
	public void setAddPanel(Component compomente) {
		this.panel.add(compomente);
	}
	
	public GroupLayout getLayout() {
		return layout;
	}
	
	public void setLayout(JPanel panel) {
		this.layout  = new GroupLayout(panel);
		this.layout.setAutoCreateGaps(true);
		this.layout.setAutoCreateContainerGaps(true);
	}
	
	public JLabel getLabelInfoFile() {
		return labelInfoFile;
	}
	
	public void setLabelInfoFile(String labelInfoFile) {
		this.labelInfoFile =  new JLabel(labelInfoFile);
	}
	//
	public JLabel getAutorL() {
		return autorL;
	}
	
	public void setAutorL(String labelAutor) {
		this.autorL = new JLabel(labelAutor);
	}
	
	public JTextField getJtautor() {
		return jtautor;
	}
	
	public void setJtautor(Integer quantidade) {
		this.jtautor  = new JTextField(quantidade);
	}
	//
	public JLabel getArquivo() {
		return arquivo;
	}
	
	public void setArquivo(String LabelArquivo) {
		this.arquivo = new JLabel(LabelArquivo);
	}
	
	public JTextField getJtarquivo() {
		return jtarquivo;
	}
	
	public void setJtarquivo(Integer quantidade) {
		this.jtarquivo = new JTextField(quantidade);
	}
	
	public JLabel getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = new JLabel(descricao);
	}
	
	public JTextField getJtdescricao() {
		return jtdescricao;
	}
	
	public void setJtdescricao(Integer quantidade) {
		this.jtdescricao =   new JTextField(quantidade);
	}
	
	public JLabel getLocalizacao() {
		return localizacao;
	}
	
	public void setLocalizacao(String localizacao) {
		this.localizacao = new JLabel(localizacao);
	}
	
	public JTextField getJtlocalizacao() {
		return jtlocalizacao;
	}
	
	public void setJtlocalizacao(Integer quantidade) {
		this.jtlocalizacao =  new JTextField(quantidade);
	}
	
	public JLabel getObservacoes() {
		return observacoes;
	}
	
	public void setObservacoes(String observacoes) {
		this.observacoes  = new JLabel(observacoes);
	}
	
	public JTextField getJtobservacoes() {
		return jtobservacoes;
	}
	
	public void setJtobservacoes(Integer quantidade) {
		this.jtobservacoes =new JTextField(quantidade);
	}
	
	public JLabel getSinistro() {
		return sinistro;
	}
	
	public void setSinistro(String sinistro) {
		this.sinistro  = new JLabel(sinistro);
	}
	
	public JComboBox<Sinistro> getJtsinistro() {
		return jtsinistro;
	}
	
	public void setJtsinistro(Sinistro[] sinistro) {
		this.jtsinistro = new JComboBox<>(sinistro);
	}
	
	public JLabel getLabelInfoGit() {
		return labelInfoGit;
	}
	
	public void setLabelInfoGit(String labelInfoGit) {
		this.labelInfoGit = new JLabel(labelInfoGit);
	}
	
	public JLabel getArquivoGit() {
		return arquivoGit;
	}
	
	public void setArquivoGit(String arquivoGit) {
		this.arquivoGit = new JLabel(arquivoGit);
	}
	
	public JTextField getJtarquivoGit() {
		return jtarquivoGit;
	}
	
	public void setJtarquivoGit(Integer quantidade) {
		this.jtarquivoGit = new JTextField(quantidade);
	}
	
	public JLabel getRepositorioGit() {
		return repositorioGit;
	}
	
	public void setRepositorioGit(String repositorioGit) {
		this.repositorioGit  = new JLabel(repositorioGit);
	}
	
	public JTextField getJtrepositorioGit() {
		return jtrepositorioGit;
	}
	
	public void setJtrepositorioGit(Integer quantidade) {
		this.jtrepositorioGit  = new JTextField(quantidade);
	}
	
	public JLabel getBranchGit() {
		return branchGit;
	}
	
	public void setBranchGit(String branchGit) {
		this.branchGit = new JLabel(branchGit);
	}
	
	public JTextField getJtbranchGit() {
		return jtbranchGit;
	}
	
	public void setJtbranchGit(Integer quantidade) {
		this.jtbranchGit = new JTextField(quantidade);
	}
	
	public JLabel getComentarioGit() {
		return comentarioGit;
	}
	
	public void setComentarioGit(String comentarioGit) {
		this.comentarioGit  = new JLabel(comentarioGit);
	}
	
	public JTextField getJtcomentarioGit() {
		return jtcomentarioGit;
	}
	
	public void setJtcomentarioGit(Integer quantidade) {
		this.jtcomentarioGit = new JTextField(quantidade);
	}
	
	public JButton getSalvar() {
		return salvar;
	}
	
	public void setSalvar(String salvar) {
		this.salvar = new JButton(salvar);
	}
	
	public JButton getGit() {
		return git;
	}
	
	public void setGit(String git) {
		this.git = new JButton(git);
	}
	
	public JButton getSalvarConfig() {
		return salvarConfig;
	}
	
	public void setSalvarConfig(String salvarConfig) {
		this.salvarConfig = new JButton(salvarConfig);
	}
	
	public FileControl getWf() {
		return wf;
	}
    
	public void setWf() {
		this.wf = new FileControl();
	}
	
	public String getLabelResult() {
		return labelResult;
	}
	
	public void setLabelResult(String labelResult) {
		this.labelResult = labelResult;
	}
	
	public JLabel getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = new JLabel(result);
	}
	
	
	public JTextArea getJtresult() {
		return jtresult;
	}
	
	public void setJtresult(String result,Integer linhas,Integer colunas) {
		this.jtresult = new JTextArea(result, linhas, colunas);
//		this.jtresult.setEnabled(false);

		
	}
	public String getLabelListBranchsName() {
		return labelListBranchsName;
	}
	
	public JButton getListbranchs() {
		return listbranchs;
	}
	
	public void setListbranchs(String listbranchsName) {
		this.listbranchs = new JButton(listbranchsName);
	}
	
	public String getLabelDefinaObjeto() {
		return labelDefinaObjeto;
	}
	
	public JLabel getDefinaObjeto() {
		return definaObjeto;
	}
	
	public void setDefinaObjeto(String definaObjeto) {
		this.definaObjeto = new JLabel(definaObjeto);
	}
	
	public JComboBox<Objeto> getJtdefinaobjeto() {
		return jtdefinaobjeto;
	}
	
	public void setJtdefinaobjeto(Objeto[] objeto) {
		this.jtdefinaobjeto = new JComboBox<>(objeto); 
	}
	
	public JScrollPane getScroll() {
		return scroll;
	}
	public void setScroll(JTextArea textArea) {
		this.scroll = new JScrollPane (textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	}

	
	public void configuraComponentes() {

	setFrame(getLabelFrame());	
	setPanel();
	setLayout(getPanel());
	
    setLabelInfoFile(getLabelRelatorio());
    
    setAutorL(getLabelAutor());
    setJtautor(20);    
    setAddPanel(getAutorL());
    setAddPanel(getJtautor());
 
    setArquivo(getLabelArquivoModificado());
    setJtarquivo(20);
    setAddPanel(getArquivo());
    setAddPanel(getJtautor());
    
    setDescricao(getLabelDescricao());
    setJtdescricao(20);
    setAddPanel(getDescricao());        
    setAddPanel(getJtdescricao());

    setLocalizacao(getLabelLinhasAlteradas());
    setJtlocalizacao(20);
    setAddPanel(getLocalizacao());
    setAddPanel(getJtlocalizacao());

    setObservacoes(getLabelObservacoesAdicionais());
    setJtobservacoes(400);
    setAddPanel(getObservacoes());
    setAddPanel(getJtobservacoes());
   
    setSinistro(getLabelGravidade());
    setJtsinistro(Sinistro.values());     
    setAddPanel(getSinistro());
    setAddPanel(getJtsinistro());
    
    setLabelInfoGit(getLabelGit());  
     
    setArquivoGit(getLabelArquivoGit());
    setJtarquivoGit(100);
    setAddPanel(getArquivoGit());
    setAddPanel(getJtarquivoGit());
    
    setRepositorioGit(getLabelPathRepositorio());
    setJtrepositorioGit(1000);    
    setAddPanel(getRepositorioGit());
    setAddPanel(getJtrepositorioGit());
    
    
    setBranchGit(getLabelBranch());
    setJtbranchGit(20);
    setAddPanel(getBranchGit());
    setAddPanel(getJtbranchGit());

    
    setComentarioGit(getLabelComentarios());
    setJtcomentarioGit(1000);
    setAddPanel(getComentarioGit());
    setAddPanel(getJtcomentarioGit());   
    
    setResult(getLabelResult());
    setJtresult("", 10,30);
    setAddPanel(getResult());
    setAddPanel(getJtresult());

    
    
    setSalvar(getLabelSalvarRelatorio());
    setGit(getLabelEnviarParaOGit());     
    setAddPanel(getSalvar());
    setAddPanel(getGit());
    
    setSalvarConfig(getLabelSalvarConfiguracao());
    setAddPanel(getSalvarConfig());
    
    setListbranchs(getLabelListBranchsName());   
    setAddPanel(getListbranchs());
    
    setDefinaObjeto(getLabelDefinaObjeto());
    setJtdefinaobjeto(Objeto.values());
    setAddPanel(getDefinaObjeto());
    setAddPanel(getJtdefinaobjeto());
    
    setScroll(getJtresult());
    setAddPanel(getScroll());
    
    
    

}

	public void FormatLayout() {
	getLayout().setHorizontalGroup(getLayout().createSequentialGroup()
    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.LEADING)    		
                    .addComponent(getAutorL())
                    .addComponent(getArquivo())
                    .addComponent(getDescricao())
                    .addComponent(getLocalizacao())
                    .addComponent(getObservacoes())                    
                    .addComponent(getSinistro())
                    .addComponent(getBranchGit())
                    .addComponent(getComentarioGit())
                    .addComponent(getRepositorioGit())
                    .addComponent(getDefinaObjeto())
                    .addComponent(getArquivoGit())
                    .addComponent(getResult())

    ) 
    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(getJtautor())
                    .addComponent(getJtarquivo())
                    .addComponent(getJtdescricao())
                    .addComponent(getJtlocalizacao())
                    .addComponent(getJtobservacoes())
                    .addComponent(getJtsinistro())      
                    .addComponent(getJtbranchGit())
                    .addComponent(getJtcomentarioGit())
                    .addComponent(getJtrepositorioGit())
                    .addComponent(getJtdefinaobjeto())
                    .addComponent(getJtarquivoGit())                    
                    .addComponent(getLabelInfoFile())
                    .addComponent(getLabelInfoGit())
                    .addGroup(getLayout().createSequentialGroup()
                                .addComponent(getListbranchs())
	                    		.addComponent(getGit())      
	                            .addComponent(getSalvar())
	                            .addComponent(getSalvarConfig())
                    )
                    .addComponent(getScroll())
                    //                    .addComponent(getJtresult())

    )    
   
    );

	getLayout().setVerticalGroup(getLayout().createSequentialGroup()
		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(getLabelInfoFile())
		    		) 
		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(getAutorL())
		            .addComponent(getJtautor())
		            ) 		  
		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(getArquivo())
		            .addComponent(getJtarquivo())
		            ) 
		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(getDescricao())
		            .addComponent(getJtdescricao())
		            )  
		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(getLocalizacao())
		            .addComponent(getJtlocalizacao())
		            )  
		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(getObservacoes())
		            .addComponent(getJtobservacoes())
		            ) 

		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(getSinistro())
		            .addComponent(getJtsinistro())
		            )  		
		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(getLabelInfoGit())
		            ) 
		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(getBranchGit())
		            .addComponent(getJtbranchGit())
		            )  		     
		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(getComentarioGit())
		            .addComponent(getJtcomentarioGit())
		            )  
		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(getRepositorioGit())
		            .addComponent(getJtrepositorioGit())		
		            ) 
		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(getDefinaObjeto())
		            .addComponent(getJtdefinaobjeto())
		            )  
		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(getArquivoGit())
		            .addComponent(getJtarquivoGit())
		            ) 		
		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(getGit())
		            .addComponent(getSalvar())	
                    .addComponent(getSalvarConfig())
                    .addComponent(getListbranchs())

		            )     
		    .addGroup(getLayout().createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(getResult())
                    .addComponent(getScroll())

//                    .addComponent(getJtresult())
		            )    

    );
    
    getPanel().setLayout(getLayout());   
    getFrame().getContentPane().add(getPanel()); 
    getFrame().setVisible(true);
}
	
	public void GetValue() {
		
	    getAutor().setNome(getJtautor());	    
	    
	    getHistory().setDataDaSolicitacao(LocalDate.now());
	    getHistory().setArquivo(getJtarquivo());
	    getHistory().setDescricao(getJtdescricao());
	    getHistory().setLocalizacao(getJtlocalizacao());
	    getHistory().setObservacao(getJtobservacoes());
	    getHistory().setAutor(getAutor());
	    getHistory().setSinistro(getJtsinistro());

	    getGitConf().setPathRespositorio(getJtrepositorioGit());
	    getGitConf().setArquivo(getJtarquivoGit());
	    getGitConf().setBranch(getJtbranchGit());
	    getGitConf().setComentario(getJtcomentarioGit());   
	}
	
	public void botaoCadastrar(){		
		getSalvar().addActionListener(        		
	            new ActionListener(){                  
	                        @SuppressWarnings("unchecked")
							@Override
	                        public void actionPerformed(ActionEvent e) {  
			                   	    getHistorico().put("Arquivo",getHistory().getArquivo().getText());
			                       	getHistorico().put("Descricao",getHistory().getDescricao().getText());
			                       	getHistorico().put("Localizacao",getHistory().getLocalizacao().getText());
			                       	getHistorico().put("Observacao",getHistory().getObservacao().getText());
		                       		getHistorico().put("Sinistro",getHistory().getSinistro().getSelectedItem().toString());

	                               List<JSONObject> lista = new ArrayList<>();
	                               lista.add(getHistorico());      
	                               	                           
	                               getAutores().put(getAutor().getNome().getText(), lista);
	                               getDatas().put(getHistory().getDataDaSolicitacao(),getAutores());

	                               try {
	                                   getWf().write(getAutor(),getDatas(),getHistory().getDataDaSolicitacao(),getGitConf());
	                               } catch (IOException e1) {
	                            	   new Mensagens(e1.getMessage());
	                               }
	                               
	                        }
	                    }
	        );  
			

	       
	        
	    }
		
	public String  ObjectChoice(String objeto,String escolha) {
		if(escolha =="ARQUIVO") {
			return objeto+".json" ;
		}
		if(escolha =="PASTA") {
			return objeto;
		}
		
		return ".";	
		
	}
	
    public void botaoGit(){

        getGit().addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                		
                        String branchAtual = getGitConf().getBranch().getText();
                        String comentario =String.format("%s", getGitConf().getComentario().getText());
                        String arquivo = ObjectChoice(getGitConf().getArquivo().getText(),getJtdefinaobjeto().getSelectedItem().toString());
                        String local = getGitConf().getPathRespositorio().getText();
                        

                        String particao = local.substring(0,2);
                        String vaiAteORepositorio =  String.format("cd %s", local);
                        String gitpull = "git pull";
						String SSLFALSE= "git config http.sslVerify false";
						String SSLTRUE= "git config http.sslVerify true";

                        String pegaOArquivoEAdicionaAoGit =   String.format("git add %s",arquivo);
                        String adicionarComentario = String.format("git commit -m %s", comentario);
                        String sobeOArquivoParaORepositorio = String.format("git push origin %s", branchAtual);
                       
                        File path = new File(local);                        	
                        if(!path.exists()) {  new Mensagens("Caminho Inexistente!"); return ; }                        
                        // StringBuilder result = execute(particao,
                        //         vaiAteORepositorio,
                        //         gitpull,
						// 		SSLFALSE,
						// 		SSLTRUE,
                        //         pegaOArquivoEAdicionaAoGit,
                        //         adicionarComentario,
                        //         sobeOArquivoParaORepositorio); 
						ExecuteGit result = new ExecuteGit(particao,
                                vaiAteORepositorio,
                                gitpull,
								SSLFALSE,
								SSLTRUE,
                                pegaOArquivoEAdicionaAoGit,
                                adicionarComentario,
                                sobeOArquivoParaORepositorio); 



						Thread threadCalculo =  new Thread(result, "Thread Calculador");
						threadCalculo.start();
                        // getJtresult().setText(result.toString());                        
                        // if(result.toString().substring(0,5).equals("fatal")) { new Mensagens(result.toString()); return; }
                        // new Mensagens("Arquivo enviado para o repositorio!");
                        
                     
                }
        
              });

    }

    public StringBuilder execute(String... comandos){
        StringBuilder saida = new StringBuilder();
        BufferedReader leitor;
        ProcessBuilder processos;
        Process processo;
        try {

                processos = new ProcessBuilder("cmd.exe", "/c", String.join(" && ", comandos));   
                processos.redirectErrorStream(true);
                processo = processos.start();
                processo.waitFor();
                leitor = new BufferedReader(new InputStreamReader(processo.getInputStream()));
                String linha = "";

                while ((linha = leitor.readLine()) != null) {
                  saida.append(linha).append("\n");
                } 
                
                
        } catch (IOException e1) {
                new Mensagens(e1.getMessage());
        } catch (InterruptedException e1) {
            	new Mensagens(e1.getMessage());
        }
        
        return saida;

    }

    public void SaveConfig(){
    	getSalvarConfig().addActionListener(
    			new ActionListener(){
					@SuppressWarnings("unchecked")
					@Override
					public void actionPerformed(ActionEvent e) {
							String pathconf = System.getProperty("user.dir");
							JSONObject jso= new JSONObject();
							jso.put("arquivogit",getJtarquivoGit().getText()+".json");
							jso.put("pathgit",getJtrepositorioGit().getText());
							String pathwithfilenameconf = pathconf+"\\"+"confGit.json";
							 try {								 	
								 	getWf().setPath(pathwithfilenameconf);
									getWf().toSave(jso);
                             } catch (Exception e1) {
                                 e1.printStackTrace();
                             }
					}     
    				
    			});
    }
    
    public void botaoViewBranchs() {
    	getListbranchs().addActionListener(
    			new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
                        String local = getGitConf().getPathRespositorio().getText();
                        String particao = local.substring(0,2);
                        String vaiAteORepositorio =  String.format("cd %s", local);
                        String listarBranch = "git branch -a";
                        
                        File path = new File(local);
                        if(!path.exists()) {  new Mensagens("Caminho Inexistente!"); return ; }
                        
                        StringBuilder result = execute(particao,vaiAteORepositorio,listarBranch);
                        getJtresult().setText(result.toString());


						
					}
				}
    			
    			);
    	
    }
}
