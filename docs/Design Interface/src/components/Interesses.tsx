import { useState } from 'react';
import { motion } from 'motion/react';
import { Heart, Plus, X, ArrowLeft } from 'lucide-react';
import { Badge } from './ui/badge';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { toast } from 'sonner@2.0.3';

interface InteressesProps {
  onBack: () => void;
}

// Interesses predefinidos do sistema
const SYSTEM_INTERESTS = [
  'Tecnologia',
  'Programação',
  'Design',
  'Fotografia',
  'Viagens',
  'Música',
  'Desporto',
  'Gastronomia',
  'Livros',
  'Cinema',
  'Arte',
  'Natureza',
  'Fitness',
  'Gaming',
  'Culinária',
  'Moda'
];

export function Interesses({ onBack }: InteressesProps) {
  const [selectedInterests, setSelectedInterests] = useState([
    'Tecnologia',
    'Programação',
    'Design',
    'Fotografia'
  ]);

  const toggleInterest = (interest: string) => {
    if (selectedInterests.includes(interest)) {
      setSelectedInterests(selectedInterests.filter(i => i !== interest));
    } else {
      setSelectedInterests([...selectedInterests, interest]);
    }
  };

  const handleSave = () => {
    toast('Interesses salvos com sucesso!');
    onBack();
  };

  return (
    <div className="h-full w-full bg-gradient-to-b from-gray-50 to-white flex flex-col overflow-y-auto pb-24">
      {/* Header */}
      <div className="bg-gradient-to-br from-blue-600 to-blue-700 pt-14 pb-12 px-6 rounded-b-[40px] relative overflow-hidden">
        <div className="absolute inset-0 opacity-20">
          <div className="absolute top-10 right-10 w-32 h-32 bg-white rounded-full blur-3xl" />
          <div className="absolute bottom-10 left-10 w-40 h-40 bg-blue-300 rounded-full blur-3xl" />
        </div>

        <div className="relative z-10">
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
            className="w-16 h-16 bg-white/20 backdrop-blur-md rounded-2xl flex items-center justify-center mb-4"
          >
            <Heart className="w-8 h-8 text-white" />
          </motion.div>
          <motion.h1
            initial={{ y: 20, opacity: 0 }}
            animate={{ y: 0, opacity: 1 }}
            transition={{ duration: 0.3 }}
            className="text-white text-3xl mb-2"
          >
            Meus Interesses
          </motion.h1>
          <motion.p
            initial={{ y: 20, opacity: 0 }}
            animate={{ y: 0, opacity: 1 }}
            transition={{ delay: 0.1, duration: 0.3 }}
            className="text-pink-100"
          >
            Personalize sua experiência
          </motion.p>
        </div>
      </div>

      {/* Conteúdo */}
      <div className="flex-1 px-6 pt-6 pb-6 overflow-y-auto">
        <div className="mb-6">
          <h2 className="text-gray-800 text-lg mb-4">
            Selecione seus interesses ({selectedInterests.length} selecionados)
          </h2>
          <p className="text-gray-500 text-sm mb-4">
            Escolha os temas que mais lhe interessam para receber anúncios relevantes
          </p>
          
          <div className="flex flex-wrap gap-2 mb-6">
            {SYSTEM_INTERESTS.map((interest, index) => (
              <motion.button
                key={interest}
                initial={{ opacity: 0, scale: 0.8 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ delay: 0.05 * index }}
                onClick={() => toggleInterest(interest)}
                type="button"
              >
                <Badge 
                  className={`px-4 py-2 text-sm cursor-pointer transition-colors ${
                    selectedInterests.includes(interest)
                      ? 'bg-blue-600 text-white hover:bg-blue-700'
                      : 'bg-gray-100 text-gray-700 hover:bg-blue-100 hover:text-blue-700'
                  }`}
                >
                  {interest}
                  {selectedInterests.includes(interest) && (
                    <X className="w-3 h-3 ml-2 inline" />
                  )}
                </Badge>
              </motion.button>
            ))}
          </div>
        </div>

        {/* Informação */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.4 }}
          className="mb-6 bg-blue-50 rounded-2xl p-5 border border-blue-100"
        >
          <h3 className="text-blue-800 mb-2">Por que escolher interesses?</h3>
          <p className="text-blue-600 text-sm">
            Seus interesses nos ajudam a personalizar os anúncios que você vê, tornando sua experiência mais relevante e interessante.
          </p>
        </motion.div>

        {/* Botão Salvar */}
        <Button
          onClick={handleSave}
          className="w-full h-14 bg-blue-600 hover:bg-blue-700 text-white"
        >
          Salvar Alterações
        </Button>
      </div>
    </div>
  );
}
