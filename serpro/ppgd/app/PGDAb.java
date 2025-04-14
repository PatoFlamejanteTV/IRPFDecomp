package serpro.ppgd.app;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.VisualizadorHelp;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.SplashScreen;
import serpro.ppgd.irpf.gui.util.GuiUtil;
import serpro.ppgd.irpf.util.AplicacaoPropertiesUtil;
import serpro.ppgd.irpf.util.MensagemUtil;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.UtilitariosArquivo;

public abstract class PGDAb {
   protected abstract boolean getPermiteMaisDeUmaInstanciaAplicacao();

   protected abstract void posCriar(PlataformaPPGD var1);

   public PGDAb(Class<?> pJanelaPrincipal) throws IOException, InterruptedException {
      this.verificarLock();
      SplashScreen splash = new SplashScreen(UtilitariosArquivo.localizaArquivoEmClasspath("/imagens/splash.jpg"), (Frame)null, 1500);
      PlataformaPPGD.setPlataforma(new PlataformaIRPFPGD());
      final PlataformaPPGD lPlataformaPPGD = PlataformaPPGD.getPlataforma();
      lPlataformaPPGD.carrega(pJanelaPrincipal);
      IRPFFacade.getInstancia().verificarIdDeclaracoes();
      String tooltipdelay = FabricaUtilitarios.getProperties().getProperty("aplicacao.tooltipdelay", "");
      if (tooltipdelay.length() > 0) {
         try {
            ToolTipManager.sharedInstance().setDismissDelay(Integer.parseInt(tooltipdelay));
         } catch (Exception var6) {
         }
      }

      splash.setShowing(false);
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            lPlataformaPPGD.exibe();
         }
      });
      lPlataformaPPGD.getJanelaPrincipal().setExtendedState(lPlataformaPPGD.getJanelaPrincipal().getExtendedState() | 6);
      this.posCriar(lPlataformaPPGD);
   }

   protected static void verificaVersaoMinimaJava(String pVersaoRequerida) throws Exception {
      boolean versaoIncompativel = false;
      String lVersaoJava = System.getProperty("java.version");

      try {
         float lNumVersaoEspecificacaoRequerida = Float.parseFloat(pVersaoRequerida.substring(0, 3));
         int lNumVersaoSegundaRequerida = Integer.parseInt(pVersaoRequerida.charAt(4).makeConcatWithConstants<invokedynamic>(pVersaoRequerida.charAt(4)));
         if (!lVersaoJava.contains(".")) {
            lVersaoJava = lVersaoJava + ".0.0";
         }

         float lNumVersaoEspecificacao = Float.parseFloat(lVersaoJava.substring(0, 3));
         if (lNumVersaoEspecificacao < lNumVersaoEspecificacaoRequerida) {
            versaoIncompativel = true;
         } else if (lNumVersaoEspecificacao == lNumVersaoEspecificacaoRequerida) {
            int lNumVersaoSegunda = Integer.parseInt(lVersaoJava.charAt(4).makeConcatWithConstants<invokedynamic>(lVersaoJava.charAt(4)));
            if (lNumVersaoSegunda < lNumVersaoSegundaRequerida) {
               versaoIncompativel = true;
            }
         }
      } catch (Exception var7) {
         LogPPGD.erro(var7.getMessage());
         versaoIncompativel = true;
      }

      if (versaoIncompativel) {
         String msg = MensagemUtil.getMensagem("VersaoJavaIncompativel", new String[]{lVersaoJava, pVersaoRequerida});
         throw new Exception(msg);
      }
   }

   public static void verificaVersaoMinimaJava() throws Exception {
      boolean versaoIncompativel = false;
      String lVersaoJava = System.getProperty("java.version");

      String msg;
      try {
         if (!lVersaoJava.contains(".")) {
            lVersaoJava = lVersaoJava + ".0.0";
         }

         if (!lVersaoJava.contains("_")) {
            lVersaoJava = lVersaoJava + "_00";
         }

         float lNumVersaoEspecificacao = Float.parseFloat(lVersaoJava.substring(0, 3));
         msg = "1.7.0_111";
         if (lNumVersaoEspecificacao == Float.valueOf("1.8")) {
            msg = "1.8.0_101";
         }

         float lNumVersaoEspecificacaoRequerida = Float.parseFloat(msg.substring(0, 3));
         int lNumVersaoSegundaRequerida = Integer.parseInt(msg.charAt(4).makeConcatWithConstants<invokedynamic>(msg.charAt(4)));
         int lNumVersaoBuildRequerida = Integer.parseInt(msg.substring(6).makeConcatWithConstants<invokedynamic>(msg.substring(6)));
         if (lNumVersaoEspecificacao < lNumVersaoEspecificacaoRequerida) {
            versaoIncompativel = true;
         } else if (lNumVersaoEspecificacao == lNumVersaoEspecificacaoRequerida) {
            int lNumVersaoSegunda = Integer.parseInt(lVersaoJava.charAt(4).makeConcatWithConstants<invokedynamic>(lVersaoJava.charAt(4)));
            if (lNumVersaoSegunda < lNumVersaoSegundaRequerida) {
               versaoIncompativel = true;
            }

            int lNumVersaoBuild = Integer.parseInt(lVersaoJava.substring(6).makeConcatWithConstants<invokedynamic>(lVersaoJava.substring(6)));
            if (lNumVersaoBuild < lNumVersaoBuildRequerida) {
               versaoIncompativel = true;
            }
         }
      } catch (Exception var9) {
         LogPPGD.erro(var9.getMessage());
         versaoIncompativel = true;
      }

      String archModel = System.getProperty("sun.arch.data.model");
      if (archModel != null && archModel.equals("64")) {
         archModel = "(64 bits)";
      } else {
         archModel = "";
      }

      if (versaoIncompativel) {
         msg = MensagemUtil.getMensagem("VersaoBuildJavaIncompativel", new String[]{lVersaoJava, archModel});
         throw new Exception(msg);
      }
   }

   protected static void verificarSO() throws Exception {
      if (System.getProperty("os.name").toLowerCase().startsWith("windows 95")) {
         String msg = MensagemUtil.getMensagem("ErroCompatibilidadeSO");
         throw new Exception(msg);
      }
   }

   protected static void mostrarHelp() {
      VisualizadorHelp visualizador = new VisualizadorHelp();
      visualizador.exibe();
   }

   protected void verificarLock() throws IOException {
      if (!this.getPermiteMaisDeUmaInstanciaAplicacao() && verificaPGDEmExecucao()) {
         GuiUtil.mostrarAviso("CopiaJaEmExecucao", new String[]{AplicacaoPropertiesUtil.getTituloVersao()});
         System.exit(1);
      }

   }

   protected static boolean verificaPGDEmExecucao() throws IOException {
      boolean ret = false;
      String arquivoLock = null;
      if (!FabricaUtilitarios.isLinux() && !FabricaUtilitarios.isMac()) {
         arquivoLock = UtilitariosArquivo.getPathAplicacao();
      } else {
         arquivoLock = UtilitariosArquivo.getPathUsuario();
      }

      File diretorio = new File(arquivoLock);
      if (!diretorio.exists() && !diretorio.mkdirs()) {
         throw new IOException("Ocorreu um erro ao criar arquivo TryLock!");
      } else {
         arquivoLock = arquivoLock + "TryLock" + ConstantesGlobais.EXERCICIO + ".txt";
         RandomAccessFile raf = new RandomAccessFile(arquivoLock, "rw");

         try {
            FileLock lock = raf.getChannel().tryLock();
            if (lock == null) {
               ret = true;
            }
         } catch (Throwable var7) {
            try {
               raf.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }

            throw var7;
         }

         raf.close();
         return ret;
      }
   }
}
