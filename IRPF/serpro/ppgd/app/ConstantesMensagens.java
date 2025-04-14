package serpro.ppgd.app;

import java.util.HashMap;
import java.util.Map;

public class ConstantesMensagens {
   private static Map<String, String> mensagens = null;

   private ConstantesMensagens() {
   }

   public static Map<String, String> getMensagens() {
      if (mensagens == null) {
         carregarMensagens();
      }

      return mensagens;
   }

   private static void carregarMensagens() {
      mensagens = new HashMap();
      mensagens.put(ConstantesMensagens.ConstantesEnum.MSG_ATUALIZAR_ERRO_VERIFICACAO.name(), "atualizar.erro.verificacao");
      mensagens.put(ConstantesMensagens.ConstantesEnum.MSG_TAREFA_RETORNOU_ERRO.name(), "msg_tarefa_retornou_erro");
   }

   public static enum ConstantesEnum {
      MSG_ATUALIZAR_ERRO_VERIFICACAO,
      MSG_TAREFA_RETORNOU_ERRO;

      public String getChave() {
         return (String)ConstantesMensagens.getMensagens().get(this.name());
      }
   }
}
