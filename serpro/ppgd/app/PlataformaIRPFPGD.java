package serpro.ppgd.app;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import serpro.ppgd.app.acoes.AtivarRecuperarNomesAction;
import serpro.ppgd.app.acoes.SairAction;
import serpro.ppgd.app.acoes.VerificarNovaVersaoAction;
import serpro.ppgd.cacheni.CacheNI;
import serpro.ppgd.infraestrutura.JanelaPrincipalPPGD;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.exception.AplicacaoException;
import serpro.ppgd.irpf.gui.ControladorGui;
import serpro.ppgd.irpf.gui.Toast;
import serpro.ppgd.irpf.gui.update.PainelAvisoVerificarAtualizacoes;
import serpro.ppgd.irpf.gui.util.GuiUtil;
import serpro.ppgd.irpf.util.update.IRPFUpdateProperties;
import serpro.ppgd.irpf.util.update.IRPFUpdater;
import serpro.ppgd.negocio.util.LogPPGD;

public class PlataformaIRPFPGD extends PlataformaPPGD {
   private SairAction fecharAction = new SairAction();

   public void setHelpID(Container componente, String id) {
   }

   public void setHelpID(JComponent componente, String id) {
   }

   public void setHelpID(JComponent componente, String id, String filePdf) {
   }

   public void carrega(Class<?> classJanelaPGD) {
      try {
         setEmDesign(false);
         this.configuraFontes();
         this.setAplicativo((JanelaPrincipalPPGD)classJanelaPGD.newInstance());
         this.carregaHelp();
         this.getJanelaPrincipal().setDefaultCloseOperation(0);
         this.getJanelaPrincipal().addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
               PlataformaIRPFPGD.this.fecharAction.actionPerformed((ActionEvent)null);
            }

            public void windowOpened(WindowEvent e) {
               boolean cacheLigado = ControladorGui.getIRPFPreferences().getBoolean("CacheNILigado", false);
               CacheNI.getInstancia().setLigado(cacheLigado);

               try {
                  CacheNI.getInstancia().carregar();
               } catch (Exception var4) {
                  LogPPGD.erro(var4.getMessage());
               }

               (new AtivarRecuperarNomesAction()).confirmaAcaoMensagemInicial();
               if (!PlataformaIRPFPGD.this.finalizouAtualizacao()) {
                  PlataformaIRPFPGD.this.verificarNovaVersao();
               }

            }
         });
      } catch (Exception var3) {
         ControladorGui.tratarException(var3);
      }

   }

   private boolean finalizouAtualizacao() {
      boolean finalizouAtualizacao = false;
      IRPFUpdater irpfUpdater = IRPFUpdater.getInstance();
      if (irpfUpdater.temPermissaoEscritaPastaBase()) {
         IRPFUpdateProperties updateProperties = null;

         try {
            if ((updateProperties = irpfUpdater.temAtualizacaoCompletada()) != null) {
               Toast.makeText(ControladorGui.getJanelaPrincipal(), "IRPF Atualizado com sucesso. Versão " + updateProperties.getVersaoAtual() + ".").display();
               if (updateProperties.getMensagem() != null && !updateProperties.getMensagem().trim().isEmpty()) {
                  finalizouAtualizacao = true;
                  GuiUtil.exibeDialog(this.getJanelaPrincipal(), new PainelAvisoVerificarAtualizacoes("Nova Versão Instalada", updateProperties.getMensagem(), (String)null, "OK"), true, "Verificar Atualizações", false, (Integer)null, (Integer)null, false, 0, true, false);
               }
            }
         } catch (AplicacaoException var5) {
         }
      }

      return finalizouAtualizacao;
   }

   private void verificarNovaVersao() {
      VerificarNovaVersaoAction action = new VerificarNovaVersaoAction();
      action.setExibirDicaAtualizar(true);
      action.setExibirPopups(false);
      action.verificarUltimaVersao();
   }

   public void aplicaArvore(String aFonteArvore) {
   }
}
