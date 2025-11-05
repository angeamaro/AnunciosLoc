import { motion } from 'motion/react';
import { ArrowLeft, FileText, Shield, Eye, AlertCircle } from 'lucide-react';
import { ScrollArea } from './ui/scroll-area';

interface PoliticasProps {
  onBack: () => void;
}

export function Politicas({ onBack }: PoliticasProps) {
  const sections = [
    {
      icon: Shield,
      title: 'Termos de Uso',
      content: 'Ao utilizar o AnunciosLoc, você concorda em respeitar as regras da comunidade. É proibido publicar conteúdo ofensivo, spam ou informações falsas. Os anúncios devem ser relevantes aos locais cadastrados.'
    },
    {
      icon: Eye,
      title: 'Privacidade',
      content: 'Seus dados de localização são utilizados apenas para fornecer funcionalidades do aplicativo. Não compartilhamos suas informações pessoais com terceiros sem seu consentimento. Você pode desativar o rastreamento de localização nas configurações.'
    },
    {
      icon: AlertCircle,
      title: 'Responsabilidade',
      content: 'O AnunciosLoc não se responsabiliza pelo conteúdo publicado pelos usuários. Cada utilizador é responsável pelos anúncios que cria. Conteúdos inapropriados devem ser reportados.'
    },
    {
      icon: FileText,
      title: 'O que é ser uma Mula?',
      content: 'Uma "Mula" no AnunciosLoc é um utilizador que auxilia na distribuição descentralizada de anúncios. Ao ativar esta funcionalidade nas definições, você permite que seu dispositivo atue como ponto de retransmissão, ajudando a levar anúncios até locais onde não há conectividade direta. Esta função é opcional, pode ser desativada a qualquer momento, e ajuda a melhorar a cobertura e alcance dos anúncios na rede. Seu dispositivo apenas retransmite dados encriptados, mantendo sua privacidade e segurança.'
    }
  ];

  return (
    <div className="h-full w-full bg-gray-50 flex flex-col">
      {/* Header */}
      <div className="bg-gradient-to-br from-blue-600 to-blue-700 pt-12 pb-10 px-6 rounded-b-[40px]">
        <button
          onClick={onBack}
          className="text-white flex items-center space-x-2 mb-6 hover:opacity-80 transition-opacity"
        >
          <ArrowLeft className="w-5 h-5" />
          <span>Voltar</span>
        </button>
        <motion.div
          initial={{ scale: 0.8, opacity: 0 }}
          animate={{ scale: 1, opacity: 1 }}
          className="w-16 h-16 bg-white/20 rounded-2xl flex items-center justify-center mb-4"
        >
          <FileText className="w-8 h-8 text-white" />
        </motion.div>
        <motion.h1
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.3 }}
          className="text-white text-3xl"
        >
          Políticas de Utilização
        </motion.h1>
        <motion.p
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ delay: 0.1, duration: 0.3 }}
          className="text-blue-100 mt-2"
        >
          Regras e condições da aplicação
        </motion.p>
      </div>

      {/* Conteúdo */}
      <ScrollArea className="flex-1 px-6 pt-6">
        <div className="space-y-4 pb-6">
          {sections.map((section, index) => (
            <motion.div
              key={section.title}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.1 * index }}
              className="bg-white rounded-2xl p-5 shadow-md"
            >
              <div className="flex items-start space-x-4">
                <div className="w-12 h-12 bg-blue-50 rounded-xl flex items-center justify-center flex-shrink-0">
                  <section.icon className="w-6 h-6 text-blue-600" />
                </div>
                <div className="flex-1">
                  <h3 className="text-gray-800 mb-2">{section.title}</h3>
                  <p className="text-gray-600 text-sm leading-relaxed">
                    {section.content}
                  </p>
                </div>
              </div>
            </motion.div>
          ))}

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.4 }}
            className="bg-blue-50 rounded-2xl p-5 border border-blue-200"
          >
            <h3 className="text-blue-800 mb-2">Versão 1.0</h3>
            <p className="text-blue-600 text-sm">
              Última atualização: 23 de outubro de 2025
            </p>
            <p className="text-gray-600 text-sm mt-3">
              Para dúvidas ou sugestões, entre em contato através do email: suporte@anuciosloc.pt
            </p>
          </motion.div>
        </div>
      </ScrollArea>
    </div>
  );
}
