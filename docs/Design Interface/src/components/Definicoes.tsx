import { useState } from 'react';
import { motion } from 'motion/react';
import { ArrowLeft, Bell, MapPin, Wifi, Search, Truck, ExternalLink } from 'lucide-react';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { Switch } from './ui/switch';
import { toast } from 'sonner@2.0.3';
import type { User as UserType, Screen } from '../App';

interface DefinicoesProps {
  user: UserType;
  onSave: (user: UserType) => void;
  onBack: () => void;
  onNavigate: (screen: Screen) => void;
}

export function Definicoes({ user, onSave, onBack, onNavigate }: DefinicoesProps) {
  const [searchQuery, setSearchQuery] = useState('');
  const [notifications, setNotifications] = useState(true);
  const [gpsTracking, setGpsTracking] = useState(true);
  const [wifiTracking, setWifiTracking] = useState(true);
  const [mulaEnabled, setMulaEnabled] = useState(true);

  const handleSave = () => {
    toast('Configurações salvas com sucesso!');
  };

  const settings = [
    {
      id: 'notifications',
      icon: Bell,
      title: 'Notificações',
      items: [
        {
          label: 'Receber Notificações',
          description: 'Alertas de anúncios próximos',
          checked: notifications,
          onChange: setNotifications
        }
      ]
    },
    {
      id: 'location',
      icon: MapPin,
      title: 'Localização',
      items: [
        {
          label: 'Rastreamento GPS',
          description: 'Usar coordenadas geográficas',
          checked: gpsTracking,
          onChange: setGpsTracking
        },
        {
          label: 'Detecção Wi-Fi',
          description: 'Identificar redes próximas',
          checked: wifiTracking,
          onChange: setWifiTracking
        }
      ]
    },
    {
      id: 'mula',
      icon: Truck,
      title: 'Ser Mula',
      items: [
        {
          label: 'Habilitar função Mula',
          description: 'Auxilie na distribuição de anúncios',
          checked: mulaEnabled,
          onChange: setMulaEnabled,
          hasLink: true
        }
      ]
    }
  ];

  const filteredSettings = settings.filter(section => 
    section.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
    section.items.some(item => 
      item.label.toLowerCase().includes(searchQuery.toLowerCase()) ||
      item.description.toLowerCase().includes(searchQuery.toLowerCase())
    )
  );

  return (
    <div className="h-full w-full bg-gradient-to-b from-gray-50 to-white flex flex-col">
      {/* Header */}
      <div className="bg-gradient-to-br from-blue-600 to-blue-700 pt-14 pb-12 px-6 rounded-b-[40px] relative overflow-hidden">
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
          <motion.h1
            initial={{ y: 20, opacity: 0 }}
            animate={{ y: 0, opacity: 1 }}
            transition={{ duration: 0.3 }}
            className="text-white text-3xl mb-2"
          >
            Definições
          </motion.h1>
          <motion.p
            initial={{ y: 20, opacity: 0 }}
            animate={{ y: 0, opacity: 1 }}
            transition={{ delay: 0.1, duration: 0.3 }}
            className="text-blue-100"
          >
            Personalize sua experiência
          </motion.p>
        </div>
      </div>

      {/* Barra de Pesquisa */}
      <div className="px-6 pt-6">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
          <Input
            type="text"
            placeholder="Pesquisar configurações..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-11 h-12 bg-white"
          />
        </div>
      </div>

      {/* Configurações */}
      <div className="flex-1 px-6 pt-4 overflow-y-auto pb-24">
        <div className="space-y-6 pb-6">
          {filteredSettings.map((section, index) => (
            <motion.div
              key={section.id}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.1 * (index + 1) }}
              className="bg-white rounded-2xl p-5 shadow-md"
            >
              <h3 className="text-gray-800 mb-4 flex items-center space-x-2">
                <section.icon className="w-5 h-5 text-blue-600" />
                <span>{section.title}</span>
              </h3>
              <div className="space-y-4">
                {section.items.map((item, itemIndex) => (
                  <div key={itemIndex}>
                    <div className={`flex items-center justify-between ${itemIndex > 0 ? 'pt-4 border-t' : ''}`}>
                      <div className="flex-1">
                        <p className="text-gray-800">{item.label}</p>
                        <p className="text-gray-500 text-sm">{item.description}</p>
                        {item.hasLink && (
                          <button
                            type="button"
                            onClick={() => onNavigate('politicas')}
                            className="text-blue-600 text-sm mt-1 flex items-center space-x-1 hover:underline"
                          >
                            <span>Saiba mais sobre ser Mula</span>
                            <ExternalLink className="w-3 h-3" />
                          </button>
                        )}
                      </div>
                      <Switch checked={item.checked} onCheckedChange={item.onChange} />
                    </div>
                  </div>
                ))}
              </div>
            </motion.div>
          ))}

          <Button
            onClick={handleSave}
            className="w-full h-14 bg-blue-600 hover:bg-blue-700 text-white"
          >
            Salvar Alterações
          </Button>
        </div>
      </div>
    </div>
  );
}
