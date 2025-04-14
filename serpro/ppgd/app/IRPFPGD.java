package serpro.ppgd.app;

import java.awt.Component;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.UIManager;
import javax.swing.plaf.IconUIResource;
import net.sf.tinylaf.Theme;
import net.sf.tinylaf.TinyLookAndFeel;
import serpro.ppgd.infraestrutura.JanelaPrincipalPPGD;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.gui.ControladorGui;
import serpro.ppgd.irpf.gui.JanelaPrincipalIRPF;
import serpro.ppgd.irpf.gui.util.GuiUtil;
import serpro.ppgd.irpf.util.AplicacaoPropertiesUtil;
import serpro.ppgd.irpf.util.ConstantesGlobaisIRPF;
import serpro.ppgd.irpf.util.OpcoesApp;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;

public class IRPFPGD extends PGDAb {
   public static void main(String[] pArgs) throws Exception {
      String complemento;
      if (!checkJavaOK()) {
         customizarDialog(true);
         complemento = "não recomendado";

         try {
            complemento = System.getProperty("java.version").split("[.]")[0];
         } catch (Exception var4) {
         }

         if (GuiUtil.mostrarConfirmaSemQuebraDeLinha("msg_java_nao_recomendado", new String[]{AplicacaoPropertiesUtil.getExercicio(), complemento, ConstantesGlobaisIRPF.JAVA_RECOMENDADO})) {
            GuiUtil.abreURL(ConstantesGlobaisIRPF.PAGINA_DOWNLOAD_JAVA);
            return;
         }
      }

      System.setProperty("javax.xml.bind.JAXBContextFactory", "org.eclipse.persistence.jaxb.JAXBContextFactory");

      try {
         if (pArgs.length > 0 && pArgs[0].equals("--verifica")) {
            if (verificaPGDEmExecucao()) {
               System.out.print("1");
            } else {
               System.out.print("0");
            }

            return;
         }

         if (FabricaUtilitarios.isLinux() && pArgs.length > 0) {
            complemento = null;
            if (pArgs[0].equals("--leia-me")) {
               complemento = "Leia-me.htm";
            } else if (pArgs[0].equals("--ajuda")) {
               complemento = "./help/AjudaIRPF.pdf";
            }

            if (complemento != null) {
               Runtime.getRuntime().exec("xdg-open " + complemento, (String[])null);
               System.exit(1);
            }
         }
      } catch (Exception var3) {
      }

      try {
         OpcoesApp opcoes = new OpcoesApp(pArgs);
         opcoes.processaArgs();
         verificarSO();
         verificaVersaoMinimaJava("1.7.0");
         if (pArgs.length <= 0 || !pArgs[0].equals("-h") && !pArgs[0].equals("--help")) {
            new IRPFPGD();
         } else {
            mostrarHelp();
         }
      } catch (Exception var5) {
         LogPPGD.erro(var5.getMessage());
         GuiUtil.mostrarErro((Component)null, var5.getMessage());
         System.exit(1);
      } catch (Throwable var6) {
         LogPPGD.erro(var6.getMessage());
         GuiUtil.mostrarErro((Component)null, "Ocorreu um erro inesperado!\nO aplicativo será encerrado!");
         System.exit(1);
      }

   }

   public IRPFPGD() throws Exception {
      super(JanelaPrincipalIRPF.class);
   }

   protected void posCriar(PlataformaPPGD pPlataforma) {
      ControladorGui.init();
      ((JanelaPrincipalPPGD)PlataformaPPGD.getPlataforma().getJanelaPrincipal()).configurarHelp();
   }

   protected boolean getPermiteMaisDeUmaInstanciaAplicacao() {
      return OpcoesApp.IS_MULTI_EXEC;
   }

   private static boolean checkJavaOK() {
      boolean retorno = true;
      if (!System.getProperty("java.version").startsWith(ConstantesGlobaisIRPF.JAVA_RECOMENDADO)) {
         retorno = false;
      }

      return retorno;
   }

   private static void customizarDialog(boolean flat) {
      try {
         Toolkit.getDefaultToolkit().setDynamicLayout(true);
         if (System.getProperty("os.name").toUpperCase().indexOf("VISTA") < 0) {
            System.setProperty("sun.awt.noerasebackground", "true");
         }

         UIManager.setLookAndFeel("net.sf.tinylaf.TinyLookAndFeel");
         URL lUrl;
         if (flat) {
            lUrl = TinyLookAndFeel.class.getResource("/Default-flat.theme");
            Theme.loadTheme(lUrl);
         } else {
            lUrl = TinyLookAndFeel.class.getResource("/Default-orig.theme");
            Theme.loadTheme(lUrl);
         }
      } catch (Exception var2) {
         LogPPGD.erro(var2.getMessage());
      }

      UIManager.put("OptionPane.yesButtonText", "Fechar o Programa e Baixar o Java " + ConstantesGlobaisIRPF.JAVA_RECOMENDADO);
      UIManager.put("OptionPane.yesButtonMnemonic", "70");
      UIManager.put("OptionPane.noButtonText", "Continuar Executando o Programa");
      UIManager.put("OptionPane.noButtonMnemonic", "67");
      UIManager.put("OptionPane.errorIcon", new IconUIResource(GuiUtil.getImage("/icones/erro.png")));
      UIManager.put("OptionPane.informationIcon", new IconUIResource(GuiUtil.getImage("/icones/info.png")));
      UIManager.put("OptionPane.questionIcon", new IconUIResource(GuiUtil.getImage("/icones/pergunta.png")));
      UIManager.put("OptionPane.warningIcon", new IconUIResource(GuiUtil.getImage("/icones/atencao.png")));
   }
}
