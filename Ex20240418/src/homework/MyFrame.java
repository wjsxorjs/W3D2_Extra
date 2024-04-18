package homework;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.awt.CardLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class MyFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField id_tf;
	private JPasswordField pw_tf;
	
	private CardLayout card;
	SqlSessionFactory factory;
	private JTextField search_tf;
	private JTable table;
	List<EmpVO> list;
	String[] c_name = {"사번","이름","직책","부서명","근무장소","입사일"};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyFrame frame = new MyFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MyFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		card = new CardLayout(0, 0);
				
		setContentPane(contentPane);
		contentPane.setLayout(card);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "card1");
		panel.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(MyFrame.class.getResource("/images/chat2.png")));
		panel.add(lblNewLabel, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(3, 1, 0, 0));
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel_1.add(panel_2);
		
		JLabel lblNewLabel_1 = new JLabel("아이디 : ");
		panel_2.add(lblNewLabel_1);
		
		id_tf = new JTextField();
		panel_2.add(id_tf);
		id_tf.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		panel_1.add(panel_3);
		
		JLabel lblNewLabel_2 = new JLabel("비밀번호 : ");
		panel_3.add(lblNewLabel_2);
		
		pw_tf = new JPasswordField();
		pw_tf.setColumns(10);
		panel_3.add(pw_tf);
		
		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_4.getLayout();
		flowLayout_2.setAlignment(FlowLayout.RIGHT);
		panel_1.add(panel_4);
		
		JButton login_bt = new JButton("로그인");
		login_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 사용자가 입력한 아이디와 비밀번호를 가져온다.
				
				String id = id_tf.getText().trim();
				String pw = new String(pw_tf.getPassword()).trim();
				
				// 아이디와 비밀번호가 입력되었는지 유효성 검사
				if(id.length() <1) {
					JOptionPane.showMessageDialog(MyFrame.this, "아이디를 입력하세요.");
					id_tf.setText(""); // 입력란 지워주기
					id_tf.requestFocus(); // 커서 이동
					return;
				} else if(pw.length() <1) {
					JOptionPane.showMessageDialog(MyFrame.this, "비밀번호를 입력하세요.");
					pw_tf.setText(""); // 입력란 지워주기
					pw_tf.requestFocus(); // 커서 이동
					return;
				} 
								
				// 현재 창의 contentPane을 얻어내어
				// Layout을 얻어낸 후 
				// 원하는 카드를 보여달라고 하면 된다.
				
				
				MemberVO vo = login(id,pw);
				
				if(vo != null) {
					card.show(contentPane, "card2");
				} else {
					// vo가 null이라는 것은 아이디 또는 비밀번호가 잘못되었다는 것
					JOptionPane.showMessageDialog(MyFrame.this, "아이디나 비밀번호가 일치하지 않습니다.");
				}
			}
		});
		panel_4.add(login_bt);
		
		
		JPanel panel_5 = new JPanel();
		contentPane.add(panel_5, "card2");
		panel_5.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_6 = new JPanel();
		panel_5.add(panel_6, BorderLayout.NORTH);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"사번", "이름", "직종", "부서"}));
		panel_6.add(comboBox);
		
		search_tf = new JTextField();
		panel_6.add(search_tf);
		search_tf.setColumns(10);
		
		JButton btnNewButton = new JButton("검색");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String keyword = search_tf.getText().trim();
				if(keyword.length()>0) {
					SqlSession ss = factory.openSession();
					if(list != null) {
						list.clear();
					}
					
					switch(comboBox.getSelectedIndex()) {
					case 0:
						list = ss.selectList("emp.search_empno",keyword);
						break;
					case 1:
						list = ss.selectList("emp.search_ename",keyword);
						break;
					case 2:
						list = ss.selectList("emp.search_job",keyword);
						break;
					case 3:
						list = ss.selectList("emp.search_deptno",keyword);
						break;
					}
					
					if(list.size()>0) {
						listTable(list);
					} else {
						JOptionPane.showMessageDialog(MyFrame.this, "검색결과가 존재하지않습니다.");
					}
					
					
					if(ss != null) {
						ss.close();
					}
				} else {
					JOptionPane.showMessageDialog(MyFrame.this, "검색어를 입력하세요.");
				}
				
			}
		});
		panel_6.add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		panel_5.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		try {
			makeFactory();
			setTitle("준비완료!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	private void makeFactory() throws Exception {
		// 환경설정파일과 연결된 스트림 준비
		Reader r = Resources.getResourceAsReader("homework/config.xml");
		
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		factory = builder.build(r);
		r.close();

	}
	
	private MemberVO login(String id, String pw) {
		// 인자로 넘어온 id, pw를 map 구조에 담아서
		// mapper인 member.login이라는 SQL문을 수행하는 것이 목적
		SqlSession ss = factory.openSession();
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("key_id", id);
		map.put("key_pw", pw);
		
		
		
		return ss.selectOne("member.login",map);

	}
	
	private void listTable(List<EmpVO> list) {
		if(table.getRowCount() > 0) {
			table.removeAll();
		}
		
		String[][] ar2 = new String[list.size()][6];
		for(int i=0; i<ar2.length; i++) {
			
			// 리스트로부터 하나의 요소를 얻어낸다.
			EmpVO vo = list.get(i);
			
			// 배열에 저장시킨다.
			ar2[i][0] = vo.getEmpno(); 		// 사번
			ar2[i][1] = vo.getEname(); 		// 이름
			ar2[i][2] = vo.getJob();		// 직책
			ar2[i][3] = vo.getDname();		// 부서명
			ar2[i][4] = vo.getCity();		// 근무장소
			ar2[i][5] = vo.getHiredate(); 	// 입사일
						
		}
		
		table.setModel(new DefaultTableModel(ar2, c_name));
	}
		
		


	
	


} // 클래스의 끝
