import { useState } from 'react';
import { motion } from 'motion/react';
import { ArrowLeft, Lock, Eye, EyeOff } from 'lucide-react';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { Card } from './ui/card';
import { toast } from 'sonner@2.0.3';

interface AlterarPasswordProps {
  onBack: () => void;
}

export function AlterarPassword({ onBack }: AlterarPasswordProps) {
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (newPassword.length < 6) {
      toast.error('A palavra-passe deve ter pelo menos 6 caracteres');
      return;
    }

    if (newPassword !== confirmPassword) {
      toast.error('As palavras-passe não coincidem');
      return;
    }

    // Aqui seria feita a chamada para atualizar a palavra-passe
    toast.success('Palavra-passe alterada com sucesso!');
    onBack();
  };

  return (
    <div className="h-full w-full bg-gradient-to-b from-gray-50 to-white flex flex-col overflow-y-auto pb-24">
      {/* Header */}
      <div className="bg-gradient-to-br from-blue-600 to-blue-700 pt-14 pb-20 px-6 rounded-b-[40px] relative overflow-hidden">
        <div className="absolute inset-0 opacity-20">
          <div className="absolute top-10 right-10 w-32 h-32 bg-white rounded-full blur-3xl" />
        </div>

        <div className="relative z-10">
          <button
            onClick={onBack}
            className="text-white flex items-center space-x-2 mb-6 hover:opacity-80 transition-opacity"
          >
            <ArrowLeft className="w-5 h-5" />
            <span>Voltar</span>
          </button>

          <div className="flex flex-col items-center">
            <motion.div
              initial={{ scale: 0.8, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              className="w-20 h-20 bg-white/20 backdrop-blur-xl rounded-full flex items-center justify-center shadow-2xl border-2 border-white/30 mb-4"
            >
              <Lock className="w-10 h-10 text-white" />
            </motion.div>

            <motion.h1
              initial={{ y: 20, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              transition={{ delay: 0.1 }}
              className="text-white text-2xl mb-2"
            >
              Alterar Palavra-Passe
            </motion.h1>
            <motion.p
              initial={{ y: 20, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              transition={{ delay: 0.2 }}
              className="text-blue-100 text-center text-sm px-8"
            >
              Digite sua nova palavra-passe
            </motion.p>
          </div>
        </div>
      </div>

      {/* Formulário */}
      <div className="flex-1 px-6 -mt-12">
        <Card className="bg-white rounded-3xl shadow-xl p-6 border-0">
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <Label htmlFor="new-password" className="text-gray-700">
                Nova Palavra-Passe
              </Label>
              <div className="relative mt-2">
                <Input
                  id="new-password"
                  type={showNewPassword ? 'text' : 'password'}
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  className="h-14 pr-12 rounded-2xl border-gray-200"
                  placeholder="Digite a nova palavra-passe"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowNewPassword(!showNewPassword)}
                  className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
                >
                  {showNewPassword ? (
                    <EyeOff className="w-5 h-5" />
                  ) : (
                    <Eye className="w-5 h-5" />
                  )}
                </button>
              </div>
              <p className="text-gray-500 text-xs mt-2">
                Mínimo de 6 caracteres
              </p>
            </div>

            <div>
              <Label htmlFor="confirm-password" className="text-gray-700">
                Confirmar Nova Palavra-Passe
              </Label>
              <div className="relative mt-2">
                <Input
                  id="confirm-password"
                  type={showConfirmPassword ? 'text' : 'password'}
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  className="h-14 pr-12 rounded-2xl border-gray-200"
                  placeholder="Digite novamente a palavra-passe"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                  className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
                >
                  {showConfirmPassword ? (
                    <EyeOff className="w-5 h-5" />
                  ) : (
                    <Eye className="w-5 h-5" />
                  )}
                </button>
              </div>
            </div>

            <div className="pt-4">
              <Button
                type="submit"
                className="w-full h-14 bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-700 hover:to-blue-800 text-white rounded-2xl shadow-lg transition-all"
              >
                Salvar Alterações
              </Button>
            </div>
          </form>

          {/* Dicas de Segurança */}
          <div className="mt-6 pt-6 border-t border-gray-100">
            <p className="text-gray-600 text-sm mb-3">Dicas de segurança:</p>
            <ul className="space-y-2 text-gray-500 text-xs">
              <li className="flex items-start">
                <span className="text-blue-600 mr-2">•</span>
                <span>Use uma combinação de letras, números e símbolos</span>
              </li>
              <li className="flex items-start">
                <span className="text-blue-600 mr-2">•</span>
                <span>Não use a mesma palavra-passe de outras contas</span>
              </li>
              <li className="flex items-start">
                <span className="text-blue-600 mr-2">•</span>
                <span>Evite informações pessoais óbvias</span>
              </li>
            </ul>
          </div>
        </Card>
      </div>
    </div>
  );
}
