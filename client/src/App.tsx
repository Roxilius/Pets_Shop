import { ThemeProvider } from "@/components/theme-provider"
import ModeToggle from "./components/ModeToggle"

export default function App() {
  return (
    <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
      <ModeToggle/>
    </ThemeProvider>
  )
}