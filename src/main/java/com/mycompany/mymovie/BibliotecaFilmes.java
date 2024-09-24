/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.mymovie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BibliotecaFilmes {
    private Connection connection;

    public BibliotecaFilmes() {
        try {
            // Registrar o driver JDBC do MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conectar ao banco de dados
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tbl_filmes", "root", "teste123");
            criarTabelaSeNaoExistir();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    

    public void adicionarFilme(String nome, String genero, String diretor, int ano, boolean assistido, boolean favorito) {
        String sql = "INSERT INTO tbl_filmes (nome_filme, genero_filme, diretor_filme, ano_lancamento, filme_assistido, filme_favorito) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, genero);
            stmt.setString(3, diretor);
            stmt.setInt(4, ano);
            stmt.setBoolean(5, assistido);
            stmt.setBoolean(6, favorito);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Filme> listarFilmes() {
        List<Filme> filmes = new ArrayList<>();
        String sql = "SELECT * FROM tbl_filmes";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Filme filme = new Filme(
                        rs.getString("nome_filme"),
                        rs.getString("genero_filme"),
                        rs.getString("diretor_filme"),
                        rs.getInt("ano_lancamento"),
                        rs.getBoolean("filme_assistido"),
                        rs.getBoolean("filme_favorito")
                );
                filme.setAvaliacao(rs.getInt("avaliacao"));
                filmes.add(filme);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filmes;
    }

    public void marcarComoAssistido(String nome) {
        String sql = "UPDATE tbl_filmes SET filme_assistido = TRUE WHERE nome_filme = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void desmarcarComoAssistido(String nome) {
        String sql = "UPDATE tbl_filmes SET filme_assistido = FALSE WHERE nome_filme = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Filme> listarAssistidos() {
        return listarFilmesPorCondicao("filme_assistido = TRUE");
    }

    public void adicionarAosFavoritos(String nome) {
        String sql = "UPDATE tbl_filmes SET filme_favorito = TRUE WHERE nome_filme = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerDosFavoritos(String nome) {
        String sql = "UPDATE tbl_filmes SET filme_favorito = FALSE WHERE nome_filme = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Filme> listarFavoritos() {
        return listarFilmesPorCondicao("filme_favorito = TRUE");
    }

    public void removerFilme(String nome) {
        String sql = "DELETE FROM tbl_filmes WHERE nome_filme = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Filme> buscarFilme(String nome) {
        List<Filme> filmes = new ArrayList<>();
        String sql = "SELECT * FROM tbl_filmes WHERE nome_filme LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Filme filme = new Filme(
                        rs.getString("nome_filme"),
                        rs.getString("genero_filme"),
                        rs.getString("diretor_filme"),
                        rs.getInt("ano_lancamento"),
                        rs.getBoolean("filme_assistido"),
                        rs.getBoolean("filme_favorito")
                );
                filme.setAvaliacao(rs.getInt("avaliacao"));
                filmes.add(filme);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filmes;
    }

    private List<Filme> listarFilmesPorCondicao(String condicao) {
        List<Filme> filmes = new ArrayList<>();
        String sql = "SELECT * FROM tbl_filmes WHERE " + condicao;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Filme filme = new Filme(
                        rs.getString("nome_filme"),
                        rs.getString("genero_filme"),
                        rs.getString("diretor_filme"),
                        rs.getInt("ano_lancamento"),
                        rs.getBoolean("filme_assistido"),
                        rs.getBoolean("filme_favorito")
                );
                filme.setAvaliacao(rs.getInt("avaliacao"));
                filmes.add(filme);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filmes;
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS filmes (" +
                "id_filme INT AUTO_INCREMENT PRIMARY KEY, " +
                "nome_filme VARCHAR(255), " +
                "genero_filme VARCHAR(255), " +
                "diretor_filme VARCHAR(255), " +
                "ano_lancamento INT, " +
                "filme_assistido BOOLEAN, " +
                "filme_favorito BOOLEAN, " +
                "avaliacao INT)";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}





