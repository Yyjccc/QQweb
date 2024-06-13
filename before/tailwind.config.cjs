/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        './src/**/*.vue',
        './src/**/*.ts',
    ],
    corePlugins: {
        preflight: false,
    },
    darkMode: 'class',
    theme: {
        extend: {
            backgroundColor: {
                'opacity-lg': 'rgba(0,0,0,.2)',
                'opacity-sm': 'rgba(0,0,0,.9)',
            },
            transitionTimingFunction: {
                'main-banner': 'cubic-bezier(0, 0, 0.62, 1.04)'
            }
        },
    },
    plugins: [],
}
