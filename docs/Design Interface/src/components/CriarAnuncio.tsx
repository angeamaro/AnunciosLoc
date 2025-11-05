import { useState } from 'react';
import { motion } from 'motion/react';
import { ArrowLeft, MessageSquare, Calendar, Phone, Share2, Tag } from 'lucide-react';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { Textarea } from './ui/textarea';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/select';
import { RadioGroup, RadioGroupItem } from './ui/radio-group';
import { Checkbox } from './ui/checkbox';
import { Badge } from './ui/badge';
import type { Location, Announcement } from '../App';

interface CriarAnuncioProps {
  locations: Location[];
  onAdd: (announcement: Omit<Announcement, 'id' | 'createdAt' | 'authorEmail' | 'authorName'>) => void;
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

export function CriarAnuncio({ locations, onAdd, onBack }: CriarAnuncioProps) {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [locationId, setLocationId] = useState('');
  const [contacts, setContacts] = useState('');
  const [deliveryType, setDeliveryType] = useState<'centralized' | 'decentralized'>('centralized');
  const [startDate, setStartDate] = useState('');
  const [startTime, setStartTime] = useState('');
  const [endDate, setEndDate] = useState('');
  const [endTime, setEndTime] = useState('');
  const [selectedInterests, setSelectedInterests] = useState<string[]>([]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    const location = locations.find(loc => loc.id === locationId);
    if (!location) return;

    const newAnnouncement: Omit<Announcement, 'id' | 'createdAt' | 'authorEmail' | 'authorName'> = {
      title,
      content,
      locationId,
      locationName: location.name,
      deliveryPolicy: {
        requiredInterests: selectedInterests
      }
    };

    onAdd(newAnnouncement);
    onBack();
  };

  const toggleInterest = (interest: string) => {
    if (selectedInterests.includes(interest)) {
      setSelectedInterests(selectedInterests.filter(i => i !== interest));
    } else {
      setSelectedInterests([...selectedInterests, interest]);
    }
  };

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
        <motion.h1
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.3 }}
          className="text-white text-3xl"
        >
          Criar Anúncio
        </motion.h1>
        <motion.p
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ delay: 0.1, duration: 0.3 }}
          className="text-blue-100 mt-2"
        >
          Publique seu anúncio vinculado a um local
        </motion.p>
      </div>

      {/* Formulário */}
      <div className="flex-1 px-6 pt-6 overflow-y-auto pb-6">
        <form onSubmit={handleSubmit} className="space-y-6 pb-6">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.1 }}
          >
            <Label htmlFor="title">Título</Label>
            <Input
              id="title"
              type="text"
              placeholder="Ex: Promoção de Livros"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="mt-2 h-12"
              required
            />
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2 }}
          >
            <Label htmlFor="content">Descrição</Label>
            <Textarea
              id="content"
              placeholder="Descreva os detalhes do anúncio..."
              value={content}
              onChange={(e) => setContent(e.target.value)}
              className="mt-2 min-h-[120px]"
              required
            />
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.3 }}
          >
            <Label htmlFor="location">Local Vinculado</Label>
            <Select value={locationId} onValueChange={setLocationId} required>
              <SelectTrigger className="mt-2 h-12">
                <SelectValue placeholder="Selecione um local" />
              </SelectTrigger>
              <SelectContent>
                {locations.map((location) => (
                  <SelectItem key={location.id} value={location.id}>
                    {location.name}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </motion.div>

          {/* Janela de Tempo */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.4 }}
            className="bg-white rounded-2xl p-5 shadow-md"
          >
            <h3 className="text-gray-800 mb-4 flex items-center space-x-2">
              <Calendar className="w-5 h-5 text-blue-600" />
              <span>Janela de Tempo</span>
            </h3>
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <Label htmlFor="startDate" className="text-sm">Data de Início</Label>
                  <Input
                    id="startDate"
                    type="date"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                    className="mt-2 h-11"
                  />
                </div>
                <div>
                  <Label htmlFor="startTime" className="text-sm">Hora de Início</Label>
                  <Input
                    id="startTime"
                    type="time"
                    value={startTime}
                    onChange={(e) => setStartTime(e.target.value)}
                    className="mt-2 h-11"
                  />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <Label htmlFor="endDate" className="text-sm">Data de Fim</Label>
                  <Input
                    id="endDate"
                    type="date"
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)}
                    className="mt-2 h-11"
                  />
                </div>
                <div>
                  <Label htmlFor="endTime" className="text-sm">Hora de Fim</Label>
                  <Input
                    id="endTime"
                    type="time"
                    value={endTime}
                    onChange={(e) => setEndTime(e.target.value)}
                    className="mt-2 h-11"
                  />
                </div>
              </div>
            </div>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.5 }}
          >
            <Label htmlFor="contacts">Contatos</Label>
            <div className="relative mt-2">
              <Phone className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
              <Input
                id="contacts"
                type="text"
                placeholder="Email, telefone ou redes sociais"
                value={contacts}
                onChange={(e) => setContacts(e.target.value)}
                className="pl-11 h-12"
              />
            </div>
          </motion.div>

          {/* Tipo de Entrega */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.6 }}
            className="bg-white rounded-2xl p-5 shadow-md"
          >
            <h3 className="text-gray-800 mb-4 flex items-center space-x-2">
              <Share2 className="w-5 h-5 text-blue-600" />
              <span>Tipo de Entrega</span>
            </h3>
            <RadioGroup value={deliveryType} onValueChange={(value) => setDeliveryType(value as 'centralized' | 'decentralized')} className="space-y-3">
              <div className="flex items-center space-x-3 bg-gray-50 rounded-xl p-4 border-2 border-gray-200 hover:border-blue-500 transition-colors cursor-pointer">
                <RadioGroupItem value="centralized" id="centralized" />
                <label htmlFor="centralized" className="flex-1 cursor-pointer">
                  <p className="text-gray-800">Centralizada</p>
                  <p className="text-gray-500 text-sm">Entrega através de servidor central</p>
                </label>
              </div>
              <div className="flex items-center space-x-3 bg-gray-50 rounded-xl p-4 border-2 border-gray-200 hover:border-purple-500 transition-colors cursor-pointer">
                <RadioGroupItem value="decentralized" id="decentralized" />
                <label htmlFor="decentralized" className="flex-1 cursor-pointer">
                  <p className="text-gray-800">Descentralizada</p>
                  <p className="text-gray-500 text-sm">Entrega direta entre dispositivos</p>
                </label>
              </div>
            </RadioGroup>
          </motion.div>

          {/* Interesses */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.7 }}
            className="bg-white rounded-2xl p-5 shadow-md"
          >
            <h3 className="text-gray-800 mb-4 flex items-center space-x-2">
              <Tag className="w-5 h-5 text-blue-600" />
              <span>Interesses (Opcional)</span>
            </h3>
            <p className="text-gray-500 text-sm mb-4">
              Selecione os interesses relevantes para este anúncio
            </p>
            <div className="flex flex-wrap gap-2">
              {SYSTEM_INTERESTS.map((interest) => (
                <button
                  key={interest}
                  type="button"
                  onClick={() => toggleInterest(interest)}
                  className="transition-all"
                >
                  <Badge 
                    className={`px-3 py-2 cursor-pointer transition-colors ${
                      selectedInterests.includes(interest)
                        ? 'bg-blue-600 text-white hover:bg-blue-700'
                        : 'bg-gray-100 text-gray-700 hover:bg-blue-100 hover:text-blue-700'
                    }`}
                  >
                    {interest}
                  </Badge>
                </button>
              ))}
            </div>
            {selectedInterests.length > 0 && (
              <p className="text-blue-600 text-sm mt-3">
                {selectedInterests.length} interesse(s) selecionado(s)
              </p>
            )}
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.8 }}
            className="pt-4"
          >
            <Button
              type="submit"
              className="w-full h-14 bg-blue-600 hover:bg-blue-700 text-white"
            >
              Publicar Anúncio
            </Button>
          </motion.div>
        </form>
      </div>
    </div>
  );
}
