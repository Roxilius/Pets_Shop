import { ThemeProvider } from "@/components/theme-provider"
import Register from "./pages/Register"

export default function App() {
  return (
    <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
      <Register/>
    </ThemeProvider>
  )
}