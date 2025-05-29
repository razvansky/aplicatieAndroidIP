$Host.UI.RawUI.WindowTitle = "MATRIX"
$chars = @("0","1")
$width = $Host.UI.RawUI.WindowSize.Width
$height = $Host.UI.RawUI.WindowSize.Height
$columns = @()

for ($i = 0; $i -lt $width; $i++) {
    $columns += Get-Random -Minimum 0 -Maximum $height
}

while ($true) {
    for ($i = 0; $i -lt $width; $i++) {
        if ($columns[$i] -ge $height) {
            $columns[$i] = 0
        } else {
            $columns[$i]++
        }

        $cursorTop = $columns[$i]
        $char = $chars | Get-Random
        $x = $i
        $y = $cursorTop

        [System.Console]::SetCursorPosition($x, $y)
        Write-Host "$char" -NoNewline -ForegroundColor Green
    }

    Start-Sleep -Milliseconds 50
}
