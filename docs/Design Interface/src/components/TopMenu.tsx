import { MoreVertical, User, Settings, Heart, Shield, LogOut } from 'lucide-react';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from './ui/dropdown-menu';
import type { Screen } from '../App';

interface TopMenuProps {
  onNavigate: (screen: Screen) => void;
  onLogout: () => void;
}

export function TopMenu({ onNavigate, onLogout }: TopMenuProps) {
  return (
    <DropdownMenu>
      <DropdownMenuTrigger className="w-10 h-10 bg-white/20 backdrop-blur-md rounded-full flex items-center justify-center border border-white/30 hover:bg-white/30 transition-all">
        <MoreVertical className="w-5 h-5 text-white" />
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end" className="w-56 bg-white/95 backdrop-blur-xl border-gray-200 shadow-xl rounded-2xl">
        <DropdownMenuItem 
          onClick={() => onNavigate('perfil')}
          className="cursor-pointer py-3 px-4 rounded-xl"
        >
          <User className="w-5 h-5 mr-3 text-blue-600" />
          <span>Perfil</span>
        </DropdownMenuItem>
        <DropdownMenuItem 
          onClick={() => onNavigate('definicoes')}
          className="cursor-pointer py-3 px-4 rounded-xl"
        >
          <Settings className="w-5 h-5 mr-3 text-gray-600" />
          <span>Definições</span>
        </DropdownMenuItem>
        <DropdownMenuItem 
          onClick={() => onNavigate('interesses')}
          className="cursor-pointer py-3 px-4 rounded-xl"
        >
          <Heart className="w-5 h-5 mr-3 text-pink-600" />
          <span>Interesses</span>
        </DropdownMenuItem>
        <DropdownMenuItem 
          onClick={() => onNavigate('politicas')}
          className="cursor-pointer py-3 px-4 rounded-xl"
        >
          <Shield className="w-5 h-5 mr-3 text-green-600" />
          <span>Políticas</span>
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem 
          onClick={onLogout}
          className="cursor-pointer py-3 px-4 rounded-xl text-red-600 focus:text-red-600"
        >
          <LogOut className="w-5 h-5 mr-3" />
          <span>Sair</span>
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
