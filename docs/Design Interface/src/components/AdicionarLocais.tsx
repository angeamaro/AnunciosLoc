import { useState } from 'react';
import { motion } from 'motion/react';
import { ArrowLeft, MapPin, Wifi } from 'lucide-react';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { RadioGroup, RadioGroupItem } from './ui/radio-group';
import type { Location } from '../App';

interface AdicionarLocaisProps {
  onAdd: (location: Omit<Location, 'id' | 'createdAt'>) => void;
  onBack: () => void;
}

export function AdicionarLocais({ onAdd, onBack }: AdicionarLocaisProps) {
  const [name, setName] = useState('');
  const [type, setType] = useState<'gps' | 'wifi'>('gps');
  const [lat, setLat] = useState('');
  const [lng, setLng] = useState('');
  const [wifiName, setWifiName] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    const newLocation: Omit<Location, 'id' | 'createdAt'> = {
      name,
      type
    };

    if (type === 'gps') {
      newLocation.coordinates = {
        lat: parseFloat(lat),
        lng: parseFloat(lng)
      };
    } else {
      newLocation.wifiName = wifiName;
    }

    onAdd(newLocation);
    onBack();
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
          Adicionar Local
        </motion.h1>
        <motion.p
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ delay: 0.1, duration: 0.3 }}
          className="text-blue-100 mt-2"
        >
          Cadastre um novo local
        </motion.p>
      </div>

      {/* Formulário */}
      <div className="flex-1 px-6 pt-6 overflow-y-auto">
        <form onSubmit={handleSubmit} className="space-y-6 pb-6">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.1 }}
          >
            <Label htmlFor="name">Nome do Local</Label>
            <Input
              id="name"
              type="text"
              placeholder="Ex: Universidade de Lisboa"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="mt-2 h-12"
              required
            />
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2 }}
          >
            <Label>Tipo de Localização</Label>
            <RadioGroup value={type} onValueChange={(value) => setType(value as 'gps' | 'wifi')} className="mt-3 space-y-3">
              <div className="flex items-center space-x-3 bg-white rounded-xl p-4 border-2 border-gray-200 hover:border-blue-500 transition-colors cursor-pointer">
                <RadioGroupItem value="gps" id="gps" />
                <label htmlFor="gps" className="flex items-center space-x-3 flex-1 cursor-pointer">
                  <div className="w-10 h-10 bg-blue-500 rounded-lg flex items-center justify-center">
                    <MapPin className="w-5 h-5 text-white" />
                  </div>
                  <div>
                    <p className="text-gray-800">GPS (Coordenadas)</p>
                    <p className="text-gray-500 text-sm">Latitude e longitude</p>
                  </div>
                </label>
              </div>
              <div className="flex items-center space-x-3 bg-white rounded-xl p-4 border-2 border-gray-200 hover:border-purple-500 transition-colors cursor-pointer">
                <RadioGroupItem value="wifi" id="wifi" />
                <label htmlFor="wifi" className="flex items-center space-x-3 flex-1 cursor-pointer">
                  <div className="w-10 h-10 bg-purple-500 rounded-lg flex items-center justify-center">
                    <Wifi className="w-5 h-5 text-white" />
                  </div>
                  <div>
                    <p className="text-gray-800">Wi-Fi</p>
                    <p className="text-gray-500 text-sm">Nome da rede</p>
                  </div>
                </label>
              </div>
            </RadioGroup>
          </motion.div>

          {type === 'gps' ? (
            <motion.div
              key="gps-fields"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.3 }}
              className="space-y-4"
            >
              <div>
                <Label htmlFor="lat">Latitude</Label>
                <Input
                  id="lat"
                  type="number"
                  step="any"
                  placeholder="Ex: 38.7564"
                  value={lat}
                  onChange={(e) => setLat(e.target.value)}
                  className="mt-2 h-12"
                  required
                />
              </div>
              <div>
                <Label htmlFor="lng">Longitude</Label>
                <Input
                  id="lng"
                  type="number"
                  step="any"
                  placeholder="Ex: -9.1549"
                  value={lng}
                  onChange={(e) => setLng(e.target.value)}
                  className="mt-2 h-12"
                  required
                />
              </div>
              <div>
                <Label htmlFor="radius">Raio de Alcance</Label>
                <Input
                  id="radius"
                  type="text"
                  value="20 metros"
                  className="mt-2 h-12 bg-gray-100 text-gray-600"
                  disabled
                />
                <p className="text-gray-500 text-xs mt-1">Raio padrão para detecção de proximidade</p>
              </div>
            </motion.div>
          ) : (
            <motion.div
              key="wifi-fields"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.3 }}
            >
              <Label htmlFor="wifiName">Nome da Rede Wi-Fi</Label>
              <Input
                id="wifiName"
                type="text"
                placeholder="Ex: CC_Colombo_WiFi"
                value={wifiName}
                onChange={(e) => setWifiName(e.target.value)}
                className="mt-2 h-12"
                required
              />
            </motion.div>
          )}

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.4 }}
            className="pt-4"
          >
            <Button
              type="submit"
              className="w-full h-14 bg-blue-600 hover:bg-blue-700 text-white"
            >
              Adicionar Local
            </Button>
          </motion.div>
        </form>
      </div>
    </div>
  );
}
