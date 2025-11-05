import { Home, MessageSquare, Bell, MapPin, Plus } from 'lucide-react';
import { motion } from 'motion/react';
import type { Screen } from '../App';

interface BottomNavigationProps {
  currentScreen: Screen;
  onNavigate: (screen: Screen) => void;
  onCreateAnnouncement: () => void;
}

export function BottomNavigation({ currentScreen, onNavigate, onCreateAnnouncement }: BottomNavigationProps) {
  const navItems = [
    { id: 'main' as Screen, icon: Home, label: 'Home' },
    { id: 'gerir-anuncios' as Screen, icon: MessageSquare, label: 'Anúncios' },
    { id: 'center', icon: Plus, label: 'Criar' }, // Este é o botão central
    { id: 'notificacoes' as Screen, icon: Bell, label: 'Notificações' },
    { id: 'listar-locais' as Screen, icon: MapPin, label: 'Locais' }
  ];

  return (
    <div className="absolute bottom-0 left-0 right-0 bg-white border-t border-gray-200 px-4 pb-6 pt-3 rounded-t-3xl shadow-2xl">
      <div className="flex items-end justify-around">
        {navItems.map((item) => {
          const isActive = currentScreen === item.id;
          const isCenter = item.id === 'center';

          if (isCenter) {
            return (
              <motion.button
                key={item.id}
                whileTap={{ scale: 0.9 }}
                onClick={onCreateAnnouncement}
                className="relative -mt-8"
              >
                <div className="w-16 h-16 bg-gradient-to-br from-blue-500 to-blue-600 rounded-full flex items-center justify-center shadow-2xl">
                  <Plus className="w-8 h-8 text-white" strokeWidth={2.5} />
                </div>
              </motion.button>
            );
          }

          return (
            <motion.button
              key={item.id}
              whileTap={{ scale: 0.9 }}
              onClick={() => onNavigate(item.id)}
              className="flex flex-col items-center space-y-1 py-2 px-3 min-w-[60px]"
            >
              <div className={`${isActive ? 'text-blue-600' : 'text-gray-400'} transition-colors`}>
                <item.icon className="w-6 h-6" strokeWidth={isActive ? 2.5 : 2} />
              </div>
              <span className={`text-xs ${isActive ? 'text-blue-600' : 'text-gray-400'} transition-colors`}>
                {item.label}
              </span>
              {isActive && (
                <motion.div
                  layoutId="activeTab"
                  className="absolute -bottom-1 left-1/2 -translate-x-1/2 w-1 h-1 bg-blue-600 rounded-full"
                />
              )}
            </motion.button>
          );
        })}
      </div>
    </div>
  );
}
