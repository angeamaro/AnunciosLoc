import { useState } from 'react';
import { motion } from 'motion/react';
import { User, Calendar, Camera, ArrowLeft, Edit2, Lock, Check, X } from 'lucide-react';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { Card } from './ui/card';
import { Textarea } from './ui/textarea';
import { toast } from 'sonner@2.0.3';
import type { User as UserType } from '../App';

interface PerfilProps {
  user: UserType;
  onSave: (user: UserType) => void;
  onBack: () => void;
  onAlterarPassword: () => void;
}

export function Perfil({ user, onSave, onBack, onAlterarPassword }: PerfilProps) {
  const [isEditingUsername, setIsEditingUsername] = useState(false);
  const [username, setUsername] = useState(user.name);
  const [bio, setBio] = useState('Apaixonado por tecnologia e inovação. Sempre em busca de novas experiências e oportunidades.');
  const memberSince = 'Janeiro de 2024';

  const handleSaveUsername = () => {
    if (!username.trim()) {
      toast.error('Username não pode estar vazio');
      return;
    }
    onSave({ ...user, name: username });
    setIsEditingUsername(false);
    toast.success('Username atualizado com sucesso!');
  };

  const handleCancelEdit = () => {
    setUsername(user.name);
    setIsEditingUsername(false);
  };

  return (
    <div className="h-full w-full bg-gradient-to-b from-gray-50 to-white flex flex-col overflow-y-auto pb-24">
      {/* Header com Avatar */}
      <div className="bg-gradient-to-br from-blue-600 to-blue-700 pt-14 pb-24 px-6 rounded-b-[40px] relative overflow-hidden">
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
              className="relative mb-4"
            >
              <div className="w-28 h-28 bg-white rounded-full flex items-center justify-center shadow-2xl border-4 border-white/30">
                <span className="text-blue-600 text-4xl">{username.charAt(0).toUpperCase()}</span>
              </div>
              <button className="absolute bottom-0 right-0 w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center shadow-lg border-2 border-white hover:bg-blue-700 transition-colors">
                <Camera className="w-5 h-5 text-white" />
              </button>
            </motion.div>

            <motion.h1
              initial={{ y: 20, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              transition={{ delay: 0.1 }}
              className="text-white text-2xl mb-1"
            >
              {username}
            </motion.h1>
            <motion.p
              initial={{ y: 20, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              transition={{ delay: 0.2 }}
              className="text-blue-100"
            >
              {user.email}
            </motion.p>
          </div>
        </div>
      </div>

      {/* Conteúdo */}
      <div className="flex-1 px-6 -mt-12">
        {/* Card de Informações do Perfil */}
        <Card className="bg-white rounded-3xl shadow-xl p-6 border-0 mb-6">
          {/* Username */}
          <div className="mb-6">
            <div className="flex items-center justify-between mb-3">
              <Label className="text-gray-700">Username</Label>
              {!isEditingUsername && (
                <Button
                  size="sm"
                  variant="ghost"
                  onClick={() => setIsEditingUsername(true)}
                  className="rounded-full h-8 px-3 text-blue-600 hover:bg-blue-50"
                >
                  <Edit2 className="w-4 h-4 mr-1" />
                  Editar
                </Button>
              )}
            </div>
            
            {isEditingUsername ? (
              <div className="space-y-3">
                <Input
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  className="h-12 rounded-2xl border-gray-200"
                  placeholder="Digite seu username"
                />
                <div className="flex gap-2">
                  <Button
                    onClick={handleSaveUsername}
                    className="flex-1 h-10 bg-blue-600 hover:bg-blue-700 rounded-xl"
                  >
                    <Check className="w-4 h-4 mr-1" />
                    Salvar
                  </Button>
                  <Button
                    onClick={handleCancelEdit}
                    variant="outline"
                    className="flex-1 h-10 rounded-xl"
                  >
                    <X className="w-4 h-4 mr-1" />
                    Cancelar
                  </Button>
                </div>
              </div>
            ) : (
              <div className="flex items-start space-x-3 bg-gray-50 rounded-2xl p-4">
                <User className="w-5 h-5 text-gray-400 mt-0.5" />
                <div className="flex-1">
                  <p className="text-gray-800">{username}</p>
                </div>
              </div>
            )}
          </div>

          {/* Alterar Palavra-Passe */}
          <div className="mb-6">
            <Label className="text-gray-700 mb-3 block">Segurança</Label>
            <button
              onClick={onAlterarPassword}
              className="w-full flex items-center justify-between bg-gray-50 hover:bg-gray-100 rounded-2xl p-4 transition-colors group"
            >
              <div className="flex items-center space-x-3">
                <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center group-hover:bg-blue-200 transition-colors">
                  <Lock className="w-5 h-5 text-blue-600" />
                </div>
                <div className="text-left">
                  <p className="text-gray-800">Alterar Palavra-Passe</p>
                  <p className="text-gray-500 text-xs">Atualize sua senha de acesso</p>
                </div>
              </div>
              <ArrowLeft className="w-5 h-5 text-gray-400 rotate-180" />
            </button>
          </div>

          {/* Biografia */}
          <div className="mb-6">
            <Label className="text-gray-700 mb-3 block">Biografia</Label>
            <div className="bg-gray-50 rounded-2xl p-4">
              <p className="text-gray-700 text-sm leading-relaxed">{bio}</p>
            </div>
          </div>

          {/* Membro Desde */}
          <div>
            <Label className="text-gray-700 mb-3 block">Informações da Conta</Label>
            <div className="flex items-start space-x-3 bg-gradient-to-br from-blue-50 to-blue-100 rounded-2xl p-4">
              <Calendar className="w-5 h-5 text-blue-600 mt-0.5" />
              <div>
                <p className="text-gray-600 text-sm">Membro desde</p>
                <p className="text-gray-800">{memberSince}</p>
              </div>
            </div>
          </div>
        </Card>

        {/* Estatísticas */}
        <div className="grid grid-cols-3 gap-3 mb-6">
          <Card className="bg-gradient-to-br from-blue-50 to-blue-100 p-4 text-center border-0 rounded-2xl">
            <p className="text-blue-900 text-2xl mb-1">12</p>
            <p className="text-blue-600 text-xs">Anúncios</p>
          </Card>
          <Card className="bg-gradient-to-br from-blue-100 to-blue-200 p-4 text-center border-0 rounded-2xl">
            <p className="text-blue-900 text-2xl mb-1">5</p>
            <p className="text-blue-700 text-xs">Locais</p>
          </Card>
          <Card className="bg-gradient-to-br from-blue-200 to-blue-300 p-4 text-center border-0 rounded-2xl">
            <p className="text-blue-900 text-2xl mb-1">28</p>
            <p className="text-blue-800 text-xs">Guardados</p>
          </Card>
        </div>
      </div>
    </div>
  );
}
